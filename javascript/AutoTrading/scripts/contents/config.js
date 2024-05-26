const properties = ['state', 'tabIds', 'assets', 'delegate', 'market', 'active', 'link'];
const globalConfig = function () {
    function Config() {
        this.active = 'market';
        this.state = 'off';
        this.assets = [];
        this.bufferThreshold = 3;
        this.link = window.location.href;
        this.market = {
            expectPercent: 5,
            profitPercent: 5,
            lossPercent: -99
        };
        //20x
        this.delegate = {
            expectPercent: 0.5,
            profitPercent: 1,
            lossPercent: -15
        }
    }

    Config.prototype.createStrategy = function () {
        if (this.active === 'market') {
            return new StrategyMA10();
        }
        if (this.active === 'delegate') {
            return new StrategyKD();
        }
    }

    Config.prototype.profile = function () {
        if (this.active === 'market') {
            return this.market;
        }
        if (this.active === 'delegate') {
            return this.delegate;
        }
    }

    //初始化
    Config.prototype.initialize = async function () {
        let data = await chrome.storage.local.get(properties);
        Object.assign(this, data);
    }

    //持久化
    Config.prototype.persistence = async function () {
        await chrome.storage.local.set(this);
    }

    //保存值
    Config.prototype.set = async function (data) {
        Object.assign(this, data);
        await chrome.storage.local.set(data);
    }
    return new Config();
}();