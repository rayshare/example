class StrategyMA10 extends Strategy {
    constructor() {
        super();
        this.startCheck();
        this.buffer = 0;
    }

    createGesture(profitPercent, lossPercent) {
        return new MarketGesture(profitPercent, lossPercent);
    }

    executeEmpty() {
        //空
        if (this.lock()) {
            console.log("Empty operation");
            this.gesture.executeEmpty(() => this.unlock());
        }
    }

    executeFull() {
        //多
        if (this.lock()) {
            console.log("Full operation");
            this.gesture.executeFull(() => this.unlock());
        }
    }

    thresholdTrigger(price, ma10) {

    }

    //启动检查
    startCheck() {
        let maArr = ma();
        let personArr = person();
        personArr.splice(0, 1);
        let assets2 = f_assets();
        let _price = price();
        [maArr, personArr, [assets2, _price]].forEach(arr => {
            for (const num of arr) {
                if (isNaN(num)) {
                    throw `Start Error: maArr[${maArr}]\tpersonArr[${personArr}]\tassets2[${assets2}]\tprice[${_price}]`
                }
            }
        });
    }

    trigger(price) {
        let ma10 = ma()[1];
        let sub = price - ma10;
        let abs = Math.abs(sub);
        let threshold = this.threshold;
        let curThreshold = abs / ma10;
        let bufferThreshold = globalConfig.bufferThreshold;
        if (curThreshold >= threshold) {
            this.buffer++;
            console.info(`${curTime()} \t trigger true \t price/ma10: ${price}/${ma10} \t threshold: ${curThreshold}/${threshold} buffer: ${this.buffer}/${bufferThreshold}`);
        } else {
            this.buffer = 0;
        }
        if (this.buffer < bufferThreshold) {
            return;
        }
        this.buffer = 0;
        if (sub > 0) {
            //开空
            this.executeEmpty();
        } else {
            //开多
            this.executeFull();
        }
    }

    async apply(price) {
        try {
            this.check();
            this.trigger(price);
        } catch (error) {
            //console.debug(error);
            return;
        }
    }
}
