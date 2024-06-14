
import cn.hutool.core.collection.CollUtil;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import java.lang.Math;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class SpaceGeometryUtil {
    /**
     * 基于边的多边形外扩
     *
     * @param polygon 逆时针方向point集合
     * @param width   宽度
     * @return
     */
    public static List<Point> expand(List<Point> polygon, double width) {
        if (CollUtil.isEmpty(polygon) || polygon.size() < 3) {
            return Collections.emptyList();
        }

        int size = polygon.size();
        double halfPI = Math.PI / 2;
        //结果
        List<Point> result = new ArrayList<>(polygon.size());
        for (int i = 0; i < size; i++) {
            Point p1 = polygon.get((i + size - 1) % size);
            Point p2 = polygon.get(i);
            Point p3 = polygon.get((i + 1) % size);

            Vector2D vec1 = new Vector2D(p2.getX() - p1.getX(), p2.getY() - p1.getY());
            Vector2D vec2 = new Vector2D(p3.getX() - p2.getX(), p3.getY() - p2.getY());
            Vector2D normalized;
            try {
                normalized = vec1.normalize();
            } catch (MathArithmeticException e) {
                log.error(e.getMessage());
                continue;
            }

            //求夹角
            double crossProduct = p3.toVector2D().crossProduct(p1.toVector2D(), p2.toVector2D());
            double dotProduct = vec1.dotProduct(vec2);
            double angle = halfPI;
            Vector2D rotateNormalized;
            if (crossProduct > 0) {
                //凸角
                angle = (Math.PI - Math.acos(dotProduct / (vec1.getNorm() * vec2.getNorm()))) / 2;
                rotateNormalized = MathUtil.rotate(new CommonData.PointXy(normalized.getX(), normalized.getY()), -angle).toVector2D();
            } else if (crossProduct < 0) {
                //凹角
                angle = (Math.PI - Math.acos(dotProduct / (vec1.getNorm() * vec2.getNorm()))) / 2;
                rotateNormalized = MathUtil.rotate(new CommonData.PointXy(normalized.getX(), normalized.getY()), angle - Math.PI).toVector2D();
            } else {
                //共线
                rotateNormalized = MathUtil.rotate(new CommonData.PointXy(normalized.getX(), normalized.getY()), -angle).toVector2D();
            }

            //向量p2'p2的模
            double normal = width / Math.abs(Math.sin(angle));
            Vector2D vec3 = rotateNormalized.scalarMultiply(normal);
            Point _p2 = new Point(p2.getX() + vec3.getX(), p2.getY() + vec3.getY(), p2.getZ());
            result.add(_p2);
        }
        return result;
    }
}

