const properties = ['state', 'tabIds', 'assets', 'delegate', 'market', 'active', 'link'];
let active;
let toggle;
let clear;
let expectInput;
let profitInput;
let lossInput;

async function send2Content(method, storage, callback) {
    let tabs = await chrome.tabs.query({});
    let data = await chrome.storage.local.get(['tabIds', 'link']);
    let tabIds = data.tabIds;
    if (tabIds.length === 0) {
        return;
    }
    tabs.forEach(tab => {
        if (!tabIds.includes(tab.id) || tab.url !== data.link) {
            return;
        }
        console.log("send message to tabId: " + tab.id);
        chrome.tabs.sendMessage(
            tab.id,
            { dest: 'content', method: method, param: { storage: storage } },
            response => {
                if (response) {
                    callback();
                }
            }
        );
    });
}

function on() {
    [expectInput, profitInput, lossInput].forEach(input => {
        input.disabled = true;
        input.oldValue = input.value;
    });
    [active, clear].forEach(btn => {
        btn.className = 'disable';
    });
    toggle.className = 'on';
    toggle.innerHTML = '<span>Started</span>';
}

function off() {
    [expectInput, profitInput, lossInput].forEach(input => {
        input.disabled = false;
    });
    [active, clear].forEach(btn => {
        btn.className = '';
    });
    toggle.className = 'off';
    toggle.innerHTML = '<span>Stopped</span>';
}

function disable() {
    [expectInput, profitInput, lossInput].forEach(input => {
        input.disabled = true;
    });
    [active, toggle, clear].forEach(btn => {
        btn.className = 'disable';
    });
}

function activeMarket() {
    active.value = 'market';
    active.innerHTML = '<span>Market</span>';
}

function activeDelegate() {
    active.value = 'delegate';
    active.innerHTML = '<span>Delegate</span>';
}

function initData(data) {
    let profile;
    if (data.active === 'market') {
        profile = data.market;
        activeMarket();
    } else if (data.active === 'delegate') {
        profile = data.delegate;
        activeDelegate();
    } else {
        return;
    }
    expectInput.value = profile.expectPercent;
    expectInput.oldValue = expectInput.value;
    profitInput.value = profile.profitPercent;
    profitInput.oldValue = profitInput.value;
    lossInput.value = profile.lossPercent;
    lossInput.oldValue = lossInput.value;
    if (data.state === 'off') {
        off();
    }
    if (data.state === 'on') {
        on();
    }
}

function activeClickEvent() {
    if (!confirm("Switch Trade")) {
        return;
    }
    disable();
    if (active.value === 'market') {
        send2Content('stop', { active: 'delegate' }, () => {
            window.location.reload();
        });
    }
    if (active.value === 'delegate') {
        send2Content('stop', { active: 'market' }, () => {
            window.location.reload();
        });
    }
}

function buttonClickEvent() {
    if (toggle.error) {
        toggle.error = false;
        return;
    }
    let state = toggle.className;
    let storage = {};
    disable();
    if (state === 'on') {
        storage.state = 'off';
        send2Content('stop', storage, off);
    } else if (state === 'off') {
        if (expectInput.value !== expectInput.oldValue ||
            profitInput.value !== profitInput.oldValue ||
            lossInput.value !== lossInput.oldValue) {
            let profile = {
                expectPercent: Number(expectInput.value),
                profitPercent: Number(profitInput.value),
                lossPercent: Number(lossInput.value),
            };
            storage[active.value] = profile;
        }
        storage.state = 'on';
        send2Content('start', storage, on);
    }
}

function clearClickEvent() {
    if (!confirm('Clear local storage')) {
        return;
    }
    clear.className = 'disable';
    chrome.storage.local.clear(() => {
        clear.className = '';
        disable();
    });
}

function checkExpectInput(e) {
    e.preventDefault();
    let newValue = expectInput.value.trim();
    let oldValue = expectInput.oldValue;
    if (newValue === oldValue) {
        expectInput.value = newValue;
        return;
    }
    let v = Number(newValue);
    if (isNaN(v) || v <= 0) {
        expectInput.value = oldValue;
    }
}

function checkProfitInput(e) {
    e.preventDefault();
    let newValue = profitInput.value.trim();
    let oldValue = profitInput.oldValue;
    if (newValue === oldValue) {
        profitInput.value = newValue;
        return;
    }
    let v = Number(newValue);
    if (isNaN(v) || v <= 0) {
        profitInput.value = oldValue;
    }
}

function checkLossInput(e) {
    e.preventDefault();
    let newValue = lossInput.value.trim();
    let oldValue = lossInput.oldValue;
    if (newValue === oldValue) {
        lossInput.value = newValue;
        return;
    }
    let v = Number(newValue);
    if (isNaN(v) || v >= 0 || v < -100) {
        lossInput.value = oldValue;
    }
}

//加载
window.onload = async function () {
    let [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
    let data = await chrome.storage.local.get(properties);
    //不能启用
    if (!data.tabIds || !data.tabIds.includes(tab.id)) {
        return;
    }

    active = document.getElementById('active');
    toggle = document.getElementById('toggle');
    clear = document.getElementById('clear');
    expectInput = document.getElementById('expectInput');
    profitInput = document.getElementById('profitInput');
    lossInput = document.getElementById('lossInput');

    initData(data);

    //设置监听器
    active.onclick = activeClickEvent;
    toggle.onclick = buttonClickEvent;
    clear.onclick = clearClickEvent;
    expectInput.onchange = checkExpectInput;
    profitInput.onchange = checkProfitInput;
    lossInput.onchange = checkLossInput;
}