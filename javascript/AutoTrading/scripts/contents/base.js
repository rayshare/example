function observe(element, callback, attributes = true, subtree = true, childList = true, characterData = true) {
    let observer = new MutationObserver(callback);
    observer.observe(element, { attributes: attributes, childList: childList, subtree: subtree, characterData: characterData });
    return observer;
}

//监听
async function listenElement(label, rootElement, f_query, attributes = true, subtree = true, childList = true, characterData = true) {
    return new Promise((resolve, reject) => {
        let el = f_query();
        if (el) {
            console.log("Found: ", label);
            resolve(el);
            return;
        }
        let observer = new MutationObserver((mutationList, observer) => {
            let el = f_query();
            if (el) {
                observer.disconnect();
                resolve(el);
                console.log("observe stop: ", label);
            }
        });
        observer.observe(rootElement, { attributes: attributes, childList: childList, subtree: subtree, characterData: characterData });
        console.log("observe start: ", label);
    });
}

//最新k线数据
function kindle() {
    if (!kindle.prototype.initialized) {
        throw 'Not initialized';
    }
    let children = kindle.prototype.element?.children;
    let KD = [
        Number(children[2]?.textContent), //开
        Number(children[4]?.textContent), //高
        Number(children[6]?.textContent), //低
        Number(children[8]?.textContent), //收
        Number(children[10]?.textContent.replaceAll('%', '')), //涨跌幅
        Number(children[12]?.textContent.replaceAll('%', '')) //振幅
    ];
    return KD;
}
kindle.prototype.init = async function () {
    let el = await listenElement('listen kindle', document, () => {
        let el = document.querySelectorAll('[class^="legendContent"]')[0];
        let v = el?.children[1]?.textContent;
        if (v === '开') {
            return el;
        }
    });
    kindle.prototype.element = el;
    kindle.prototype.initialized = true;
    console.log("kindle init");
}

//最新ma数据
function ma() {
    if (!ma.prototype.initialized) {
        throw 'Not initialized';
    }
    let children = ma.prototype.element?.children;
    let MA = [
        Number(children[1]?.textContent), //MA5
        Number(children[3]?.textContent), //MA10
        Number(children[5]?.textContent), //MA20
        Number(children[7]?.textContent), //MA120
    ];
    return MA;
}
ma.prototype.init = async function () {
    let el = await listenElement('listen ma', document, () => {
        let el = document.querySelectorAll('[class^="legendContent"]')[1];
        let v = el?.children[0]?.textContent;
        if (v === 'MA(5)') {
            return el;
        }
    });
    ma.prototype.element = el;
    ma.prototype.initialized = true;
    console.log("ma init");
}

//最新成交量数据
function volume() {
    if (!volume.prototype.initialized) {
        throw 'Not initialized';
    }
    let children = volume.prototype.element?.children;
    let VOL = [
        Number(children[2]?.textContent.replaceAll(",", "")), //DOGE
        Number(children[4]?.textContent.replaceAll(",", "")), //USDT
    ];
    return VOL;
};
volume.prototype.init = async function () {
    let el = await listenElement('listen volume', document, () => {
        let el = document.querySelectorAll('[class^="legendContent"]')[2];
        let v = el?.children[0]?.textContent;
        if (v === 'VOLUME') {
            return el;
        }
    });
    volume.prototype.element = el;
    volume.prototype.initialized = true;
    console.log("volume init");
}

//个人仓位
function person() {
    if (!person.prototype.initialized) {
        throw 'Not initialized';
    }
    let el = person.prototype.element;
    let itemList = el.querySelectorAll('[class^="index_infoItemValue"]');
    let PERSON = [
        Number(itemList[0]?.textContent.replaceAll(/%|,/gi, '')), //保证金率
        Number(itemList[1]?.textContent.replaceAll(/usdt|,/gi, "")), //维持保证金
        Number(itemList[2]?.textContent.replaceAll(/usdt|,/gi, "")), //币种权益
        Number(itemList[3]?.textContent.replaceAll(/usdt|,/gi, "")), //浮动收益
        Number(itemList[4]?.textContent.replaceAll(/usdt|,/gi, "")), //占用
    ];
    return PERSON;
};
person.prototype.init = async function () {
    let el = await listenElement('listen person', document, () => {
        return document.querySelector('[class^="index_marginLevelContent"]');
    });
    person.prototype.element = el;
    person.prototype.initialized = true;
    console.log("person init");
}

//价格
function price() {
    if (!price.prototype.initialized) {
        throw 'Not initialized';
    }
    let el = price.prototype.element;
    return Number(el.textContent);
};
price.prototype.init = async function () {
    let el = await listenElement('listen price', document, () => {
        return document.querySelector('span[class^="index_tickerPrice"]');
    });
    price.prototype.element = el;
    price.prototype.initialized = true;
    console.log("price init");
}

async function _setTimeout(timeout, callback) {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            if (callback) {
                callback();
            }
            resolve();
        }, timeout);
    });
}

function curTime() {
    return new Date().toLocaleString();
}

function timestamp() {
    return new Date().getTime();
}