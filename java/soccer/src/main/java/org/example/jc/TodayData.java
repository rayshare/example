package org.example.jc;

import com.jayway.jsonpath.DocumentContext;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.example.util.HttpUtil;
import org.example.util.JsonUtil;
import org.example.util.Meta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class TodayData {
    private static final Logger log = LoggerFactory.getLogger(TodayData.class);
    //让球胜平负
    public static final String wdl = "https://webapi.sporttery.cn/gateway/jc/football/getMatchCalculatorV1.qry?poolCode=hhad,had&channel=c";
    //进球数
    public static final String goals = "https://webapi.sporttery.cn/gateway/jc/football/getMatchCalculatorV1.qry?poolCode=ttg&channel=c";
    //半全场
    public static final String allWdl = "https://webapi.sporttery.cn/gateway/jc/football/getMatchCalculatorV1.qry?poolCode=hafu&channel=c";
    //比分
    public static final String score = "https://webapi.sporttery.cn/gateway/jc/football/getMatchCalculatorV1.qry?poolCode=crs&channel=c";

    private Boolean success = true;

    private Map<Integer, Pair<Triple<String, String, Pair<String, String>>, List<Cell>>> data;

    public TodayData() {
        Map<Integer, Map<String, Object>> wdlDataMap = fetch(wdl);
        if (wdlDataMap.isEmpty()) {
            this.success = false;
            return;
        }
        Map<Integer, Map<String, Object>> goalsDataMap = fetch(goals);
        if (goalsDataMap.isEmpty()) {
            this.success = false;
            return;
        }
        Map<Integer, Map<String, Object>> allWdlDataMap = fetch(allWdl);
        if (allWdlDataMap.isEmpty()) {
            this.success = false;
            return;
        }
        Map<Integer, Map<String, Object>> scoreWdlDataMap = fetch(score);
        if (scoreWdlDataMap.isEmpty()) {
            this.success = false;
            return;
        }
        data = new LinkedHashMap<>();
        fillWdlData(wdlDataMap);
        fillScoreData(scoreWdlDataMap);
        fillGoalsData(goalsDataMap);
        fillAllWdlData(allWdlDataMap);
    }

    private void fillAllWdlData(Map<Integer, Map<String, Object>> allWdlDataMap) {
        log.info("merge allWdlData");
        allWdlDataMap.forEach((matchId, dataMap) -> {
            Triple<String, String, Pair<String, String>> title = getTitle(dataMap);
            List<Cell> cellList = new ArrayList<>();
            Map<String, Object> hadMap = JsonUtil.readValue(dataMap, "$.hafu");
            if (hadMap != null) {
                fillData(Meta.AWDL, hadMap, cellList);
            }
            Pair<Triple<String, String, Pair<String, String>>, List<Cell>> pair = data.computeIfAbsent(matchId, key -> Pair.of(title, new ArrayList<>()));
            pair.getRight().addAll(cellList);
        });
    }

    private void fillGoalsData(Map<Integer, Map<String, Object>> goalsDataMap) {
        log.info("merge goalsData");
        goalsDataMap.forEach((matchId, dataMap) -> {
            Triple<String, String, Pair<String, String>> title = getTitle(dataMap);
            List<Cell> cellList = new ArrayList<>();
            Map<String, Object> ttgMap = JsonUtil.readValue(dataMap, "$.ttg");
            if (ttgMap != null) {
                fillData(Meta.GOALS, ttgMap, cellList);
            }
            Pair<Triple<String, String, Pair<String, String>>, List<Cell>> pair = data.computeIfAbsent(matchId, key -> Pair.of(title, new ArrayList<>()));
            pair.getRight().addAll(cellList);
        });
    }

    private void fillScoreData(Map<Integer, Map<String, Object>> scoreWdlDataMap) {
        log.info("merge scoreWdlData");
        scoreWdlDataMap.forEach((matchId, dataMap) -> {
            Triple<String, String, Pair<String, String>> title = getTitle(dataMap);
            List<Cell> cellList = new ArrayList<>();
            Map<String, Object> crsMap = JsonUtil.readValue(dataMap, "$.crs");
            if (crsMap != null) {
                fillData(Meta.WSCORE, crsMap, cellList);
                fillData(Meta.DSCORE, crsMap, cellList);
                fillData(Meta.LSCORE, crsMap, cellList);
            }
            Pair<Triple<String, String, Pair<String, String>>, List<Cell>> pair = data.computeIfAbsent(matchId, key -> Pair.of(title, new ArrayList<>()));
            pair.getRight().addAll(cellList);
        });
    }

    private void fillWdlData(Map<Integer, Map<String, Object>> wdlDataMap) {
        log.info("merge WdlData");
        wdlDataMap.forEach((matchId, dataMap) -> {
            Triple<String, String, Pair<String, String>> title = getTitle(dataMap);
            List<Cell> cellList = new ArrayList<>();
            Map<String, Object> hadMap = JsonUtil.readValue(dataMap, "$.had");
            if (hadMap != null) {
                fillData(Meta.WDL, hadMap, cellList);
            }
            Map<String, Object> hhadMap = JsonUtil.readValue(dataMap, "$.hhad");
            if (hhadMap != null) {
                fillData(Meta.HWDL, hhadMap, cellList);
            }
            Pair<Triple<String, String, Pair<String, String>>, List<Cell>> pair = data.computeIfAbsent(matchId, key -> Pair.of(title, new ArrayList<>()));
            pair.getRight().addAll(cellList);
        });
    }

    private static Triple<String, String, Pair<String, String>> getTitle(Map<String, Object> dataMap) {
        String leagueAbbName = (String) dataMap.getOrDefault("leagueAbbName", "赛事名称");
        String matchNumDate = (String) dataMap.getOrDefault("matchNumDate", "比赛序列号");
        String matchNumStr = (String) dataMap.getOrDefault("matchNumStr", "比赛序号");
        String matchDate = (String) dataMap.getOrDefault("matchDate", "比赛日期");
        String matchTime = (String) dataMap.getOrDefault("matchTime", "比赛时间");
        String homeTeamAbbName = (String) dataMap.getOrDefault("homeTeamAbbName", "主队名称");
        String awayTeamAbbName = (String) dataMap.getOrDefault("awayTeamAbbName", "客队名称");
        String meta = String.join("  ", matchNumDate, matchDate, matchTime);
        return Triple.of(matchNumStr + " " + leagueAbbName, meta, Pair.of(homeTeamAbbName, awayTeamAbbName));
    }

    private static void fillData(Meta meta, Map<String, Object> hadMap, List<Cell> cellList) {
        int startIndex = meta.getRowNumber() * 13;
        for (int i = 0; i < meta.getPropertyArr().length; i++) {
            String property = meta.getPropertyArr()[i];
            String value = (String) hadMap.getOrDefault(property, "");
            cellList.add(new Cell(startIndex + i, value));
        }
    }

    private Map<Integer, Map<String, Object>> fetch(String url) {
        log.info("Fetch Data: {}", url);
        String wdlJson = HttpUtil.get(url);
        DocumentContext wdlContext = JsonUtil.readValue(wdlJson);
        if (wdlContext == null) {
            return Collections.emptyMap();
        }
        Boolean success = wdlContext.read("$.success", Boolean.class);
        if (!Boolean.TRUE.equals(success)) {
            return Collections.emptyMap();
        }
        JSONArray arrayOfArr = JsonUtil.jsonPath(wdlContext, "$.value.matchInfoList[*].subMatchList");
        if (arrayOfArr == null) {
            return Collections.emptyMap();
        }
        JSONArray dataList = arrayOfArr.stream().map(e -> (JSONArray) e).flatMap(JSONArray::stream).collect(
                JSONArray::new, JSONArray::add, JSONArray::merge
        );
        return dataList.stream()
                .map(e -> (Map<String, Object>) e)
                .filter(e -> Objects.nonNull(e.get("matchId")))
                .collect(Collectors.toMap(
                        e -> (int) e.get("matchId"),
                        v -> v,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Map<Integer, Pair<Triple<String, String, Pair<String, String>>, List<Cell>>> getData() {
        return data;
    }

    public void setData(Map<Integer, Pair<Triple<String, String, Pair<String, String>>, List<Cell>>> data) {
        this.data = data;
    }

    //网格
    public static class Cell {
        private Integer index;

        private String text;

        public Cell() {
        }

        public Cell(Integer index, String text) {
            this.index = index;
            this.text = text;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
