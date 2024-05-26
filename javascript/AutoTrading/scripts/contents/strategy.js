class Strategy {
    constructor() {
        this.locked = false;
        let profile = globalConfig.profile();
        this.threshold = profile.expectPercent / 100;
        this.profitPercent = profile.profitPercent;
        this.lossPercent = profile.lossPercent;
        this.gesture = this.createGesture(profile.profitPercent, profile.lossPercent);
        console.log("strategy threshold: ", this.threshold);
    }

    createGesture(profitPercent, lossPercent) {
        throw 'Illegal Function';
    }

    lock() {
        return !this.locked && (this.locked = true);
    }

    async unlock() {
        //检测运行状态，成功后解锁
        console.log("waiting unlock");
        let running;
        let personArr;
        for (let i = 1; i <= 15; i++) {
            await _setTimeout(1000, () => {
                personArr = person();
                running = personArr[0];
                console.log("waiting unlock: ", i, running);
            });
            if (running) {
                break;
            }
        }
        let assets = globalConfig.assets;
        assets.push(personArr[2]);
        await globalConfig.set({ assets: assets });
        this.locked = false;
        console.log("unlock success");
    }

    check() {
        let personArr = person(); //保证金率 维持保证金 币种权益 浮动收益 占用
        let isRunning = personArr[0]; //是否已运行
        let assets = personArr[2]; //资产
        let assets2 = f_assets(); //修复获取不到资产的错误
        //校验
        if (isRunning || !assets || assets < 1 || this.locked || !assets2 || assets2 < 1) {
            throw `Forbidden: running[${isRunning}] assets[${assets}] locked[${this.locked}]`;
        }
    }
}
