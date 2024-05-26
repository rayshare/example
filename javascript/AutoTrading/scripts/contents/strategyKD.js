class StrategyKD extends Strategy {
    constructor() {
        super();
        this.startCheck();
    }

    createGesture(profitPercent, lossPercent) {
        return new DelegateGesture(profitPercent, lossPercent);
    }

    executeFull(delegatePrice) {
        //多
        if (this.lock()) {
            console.log("Full operation");
            this.gesture.executeFull(delegatePrice, () => this.unlock());
        }
    }

    executeEmpty(delegatePrice) {
        //空
        if (this.lock()) {
            console.log("Empty operation");
            this.gesture.executeEmpty(delegatePrice, () => this.unlock());
        }
    }

    //启动检查
    startCheck() {
        let kdArr = kindle();
        let personArr = person();
        personArr.splice(0, 1);
        let assets2 = f_assets();
        let _price = price();
        [kdArr, personArr, [assets2, _price]].forEach(arr => {
            for (const num of arr) {
                if (isNaN(num)) {
                    throw `Start Error: kdArr[${kdArr}]\tpersonArr[${personArr}]\tassets2[${assets2}]\tprice[${_price}]`
                }
            }
        });
    }

    trigger(price) {
        let threshold = this.threshold;
        let threshold_half = threshold / 2;
        let kdArr = kindle();

        let riseFall = kdArr[4] / 100; //涨跌幅
        let amp = kdArr[5] / 100; //振幅
        let riseFall_abs = Math.abs(riseFall);
        if (riseFall_abs < threshold_half || amp < threshold) {
            return;
        }

        let base = riseFall > 0 ? kdArr[2] : kdArr[1];
        let amp_2 = (riseFall / riseFall_abs) * amp * 2;
        let delegatePrice = base + (base * amp_2);

        console.info(`${curTime()} \t trigger true \t riseFall>=threshold_half: ${riseFall}/${threshold_half} \t amp>=threshold: ${amp}/${threshold} \t price: ${price} delegatePrice: ${delegatePrice}`);
        if (riseFall > 0) {
            //开空
            this.executeEmpty(delegatePrice);
        } else {
            //开多
            this.executeFull(delegatePrice);
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