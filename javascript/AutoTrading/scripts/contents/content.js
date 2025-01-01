//状态
let isInited = false; //是否初始化
let isStarted = false; //是否已启动
let strategy; //策略
let tabId; //标签id
let priceObserver; //价格监听
let f_assets; //资产监听
const revoke_suffix = '/balance/report-center/unified/order-open'; //自动订单撤销

async function observeAssets() {
    let panel = await listenElement('observeAssets: leftForm', document, () => document.getElementById('leftPoForm'));
    let span = await listenElement('observeAssets: span', panel, () => {
        return panel.querySelector('div[class="avail-display-container"]')?.querySelectorAll('span[class^="index_value"]')[0];
    });
    f_assets = () => {
        return Number(span.textContent.replaceAll(/\s*usdt/gi, ''));
    };
}

//初始化
async function init() {
    if (isInited) {
        return Promise.resolve();
    }
    await kindle.prototype.init();
    await ma.prototype.init();
    await volume.prototype.init();
    await person.prototype.init();
    await price.prototype.init();
    isInited = true;
    console.log("init");
}

function stopEvent(event) {
    if ((event.type === 'keydown' || event.type === 'keyup') && event.key === 'F12') {
        return true;
    }
    event.preventDefault();
    event.stopImmediatePropagation();
    event.stopPropagation();
    return false;
}

//加入玻璃
function addGlass() {
    const glass = document.createElement('div');
    glass.style.position = 'fixed'; // 固定定位
    glass.style.top = '0'; // 顶部对齐
    glass.style.left = '0'; // 左侧对齐
    glass.style.width = '100vw'; // 全宽
    glass.style.height = '100vh'; // 全高
    glass.style.pointerEvents = 'all'; // 捕获所有鼠标事件
    glass.style.zIndex = '9007199254740991'; // 确保在其他元素之上
    glass.style.backgroundColor = 'rgba(0, 0, 0, 0)'; // 完全透明背景
    document.body.appendChild(glass);
    document.onkeydown = stopEvent;
    document.onkeyup = stopEvent;
    document.onkeypress = stopEvent;
    document.oncontextmenu = stopEvent;
}

//开启
async function start(param, callback) {
    if (!isInited) {
        await init();
    }
    if (isStarted) {
        return Promise.resolve();
    }
    //初始化策略
    if (param?.storage) {
        await globalConfig.set(param.storage);
    }
    for (let i = 0; i < 4; i++) {
        try {
            strategy = globalConfig.createStrategy();
            break;
        } catch (ignore) {
            await _setTimeout(500);
        }
    }
    if (!strategy) {
        console.error("Start Error");
        window.location.reload();
    }
    priceObserver = observe(price.prototype.element, () => strategy.apply(price()));
    isStarted = true;

    console.info("strategy start");

    if (callback) {
        callback();
    }
}

//关闭
async function stop(param, callback) {
    if (param?.storage) {
        await globalConfig.set(param.storage);
    }
    if (priceObserver) {
        priceObserver.disconnect();
    }
    strategy = undefined;
    isStarted = false;
    console.info("strategy stop");

    if (callback) {
        callback();
    }
}

//初始化
async function initialize(tabIds) {
    console.log('Message Received: ', tabIds);
    if (!tabIds) {
        return;
    }

    //初始化配置
    await globalConfig.initialize();
    globalConfig.tabIds = tabIds;
    await globalConfig.persistence();
    await observeAssets();

    //启动
    if (globalConfig.state === 'on') {
        start();
    }

    //定时重启
    setTimeout(() => {
        window.location.reload();
    }, 1800000);

    //监听消息
    chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
        if (message.dest !== 'content') {
            return true;
        }
        console.log('Message Received: ', message);
        if (this[message.method]) {
            this[message.method](message.param, () => sendResponse(true));
        }
        return true;
    });
}

let url = window.location.href;
if (url.endsWith(revoke_suffix)) {
    //撤销
    console.log("content scripts loaded");
    revoke();
} else {
    //交易
    addGlass();
    //读取到tabId，再执行后续操作
    chrome.runtime.sendMessage({ dest: 'background', method: 'getTabId' }, initialize);
}