const target_url = '/trade-swap/doge-usdt-swap';
let methods = {
    getTabId: async function (param, callback) {
        chrome.tabs.query({}, tabs => {
            let tabIds = [];
            for (const tab of tabs) {
                if (tab.url.endsWith(target_url)) {
                    tabIds.push(tab.id);
                }
            }
            callback(tabIds);
        });
    }
};

chrome.windows.onRemoved.addListener((windowId) => {
    console.log("close window:", windowId);
    chrome.storage.local.clear();
});

chrome.windows.onCreated.addListener((windowId) => {
    console.log("open window:", windowId);
    chrome.storage.local.clear();
});

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    if (message.dest === 'background') {
        console.log('Message Received: ', message);
        if (methods[message.method]) {
            methods[message.method](message.param, sendResponse);
        }
    }
    return true;
});

chrome.tabs.onRemoved.addListener((tabId, removeInfo) => {
    console.log("close tab: ", tabId);
    chrome.storage.local.get(['tabIds'], function (obj) {
        if (obj.tabIds) {
            let index = obj.tabIds.indexOf(tabId);
            if (index !== -1) {
                obj.tabIds.splice(index, 1);
                chrome.storage.local.set({ tabIds: obj.tabIds });
            }
        }
    });
});

chrome.management.onEnabled.addListener((info) => {
    if (info?.shortName === 'AutoTrading') {
        console.log("enable: ", info);
        chrome.storage.local.remove('tabIds');
    }
});