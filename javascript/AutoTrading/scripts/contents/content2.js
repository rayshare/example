const timeout_threshold = 1080; //超时委托 秒
const f_timeout_retry = () => {
    return parseInt(Math.random() * 61) + 60;
}; //重启 30~80s

function parseOrderTime(el) {
    let childList = el.children;
    let dateStr = childList[0]?.textContent.replaceAll('\/', '-');
    let timeStr = childList[1]?.textContent;
    if (dateStr && timeStr) {
        return new Date(`${dateStr} ${timeStr}`).getTime();
    }
}

async function waitingDocument() {
    let counter = 0;
    let observer = observe(document, () => {
        counter = 0;
    });
    for (; counter < 4; counter++) {
        await _setTimeout(500);
    }
    observer.disconnect();
}

async function revoke() {
    await waitingDocument();
    let rowsEl = document.querySelector('div.okui-table-container')?.querySelectorAll('tr.okui-table-row');
    console.log("page started, length: ", rowsEl?.length);
    if (rowsEl) {
        _revoke(rowsEl);
    }

    //定时重启
    let timeout_s = f_timeout_retry();
    console.log("reload after s: ", timeout_s);
    setTimeout(() => {
        window.location.reload();
    }, timeout_s * 1000);
}

function _revoke(rowsEl) {
    let cur = timestamp();
    for (const el of rowsEl) {
        let label = el.querySelector('td>span')?.textContent;
        if (label.endsWith('开空') || label.endsWith('开多')) {
            let time_el = el.querySelector('td>ul');
            let time = parseOrderTime(time_el);
            if (!time) {
                console.error('Error Time: ', time_el.innerHTML);
                continue;
            }
            let seconds = (cur - time) / 1000;
            if (seconds >= timeout_threshold) { //撤单大于timeout_threshold未成的
                let btn = el.querySelectorAll('div.table-opera button')[1];
                if (!btn) {
                    console.error('Error: Button Not Found');
                    continue;
                }
                console.log("revoke: ", time_el.textContent);
                btn.click();
            }
        }
    }
}