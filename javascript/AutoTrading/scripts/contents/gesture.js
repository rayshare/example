class Gesture {
    constructor(profitPercent, lossPercent) {
        if (!profitPercent || profitPercent <= 0) {
            throw 'Error [profitPercent] Arguments'
        }
        if (!lossPercent || lossPercent >= 0 || lossPercent < -100) {
            throw 'Error [lossPercent] Arguments'
        }
        this.profitPercent = profitPercent;
        this.lossPercent = lossPercent;
    }

    input(inputElement, newValue) {
        let oldValue = inputElement.value;
        inputElement.value = newValue;
        let event = new Event('input', { bubbles: true });
        let tracker = inputElement._valueTracker;
        if (tracker) {
            tracker.setValue(oldValue);
        }
        inputElement.dispatchEvent(event);
    }

    async queryInputPanel(label, id) {
        return await listenElement(label, document, () => document.getElementById(id));
    }

    async clickMarket(label, container) {
        container = container.parentElement.parentElement;
        let el = await listenElement(label, container, () => {
            let el = container.querySelector('div[class="okui-tabs-pane-list-flex-shrink"]');
            let childList = el?.children;
            if (childList && childList[1]?.textContent === '市价委托') {
                return childList[1];
            }
        });
        el.click();
        await listenElement('等待点击生效', el, () => {
            if (el.className.includes('okui-tabs-pane-underline-active')) {
                return el;
            }
        });
    }

    async clickDelegate(label, container) {
        container = container.parentElement.parentElement;
        let el = await listenElement(label, container, () => {
            let el = container.querySelector('div[class="okui-tabs-pane-list-flex-shrink"]');
            let childList = el?.children;
            if (childList && childList[0]?.textContent === '限价委托') {
                return childList[0];
            }
        });
        el.click();
        await listenElement('等待点击生效', el, () => {
            if (el.className.includes('okui-tabs-pane-underline-active')) {
                return el;
            }
        });
    }

    async inputDelegatePrice(label, container, delegatePrice) {
        let el = await listenElement(label, container, () => container.querySelector('input[type="text"]'));
        this.input(el, delegatePrice);
    }

    async inputSellPrice(label, container, sellPrice) {
        let el = await listenElement(label, container, () => container.querySelectorAll('input[type="text"]')[2]);
        this.input(el, sellPrice);
    }

    async inputLossPrice(label, container, lossPrice) {
        let el = await listenElement(label, container, () => container.querySelectorAll('input[type="text"]')[3]);
        this.input(el, lossPrice);
    }

    async clickQuantity(label, container) {
        let slider = await listenElement(label, container, () => container.querySelectorAll('span[class^="InputSlider_nodeText"]')[4]);
        slider.click();
    }

    async clickProfitLoss(label, container) {
        let checkBox = await listenElement(label, container, () => container.querySelector('input[class="okui-checkbox-input"]'));
        if (!checkBox.checked) {
            checkBox.click();
        }
        return checkBox.parentElement.parentElement.parentElement;
    }

    async clickAdvanceProfit(label, container) {
        let advance = await listenElement(label, container, () => container.querySelector('div[class^="Entry_operatorIcon"]'));
        advance.click();
    }

    async queryProfitDialogPanel(label) {
        return await listenElement(label, document, () => document.getElementById("scroll-box"))
    }

    async inputProfitPercent(label, container, profitPercent) {
        let profitInput = await listenElement(label, container, () => container.querySelectorAll('div[class="okui-input-box"]')[1]?.querySelector('input[type="text"]'));
        this.input(profitInput, profitPercent);
    }

    async clickLossOption(label, container) {
        let optionArrow = await listenElement(label, container, () => container.querySelectorAll('div[class="okui-input-box"]')[3]?.querySelector('i'));
        optionArrow.click();
    }

    async queryLossDialog(label) {
        return await listenElement(label, document, () => document.querySelectorAll("#scroll-box")[1]);
    }

    async clickLossRate(label, container) {
        let optionItem = await listenElement(label, container, () => {
            let span = container.querySelectorAll('span')[1];
            if (span && span.textContent === '收益率 (%)') {
                return span;
            } else {
                return undefined;
            }
        });
        optionItem.click();
    }

    async clickLossDialogConfirm(label, container) {
        let confirmBtn = await listenElement(label, container, () => {
            return container.nextElementSibling?.querySelector('button[data-testid="okd-dialog-confirm-btn"]');
        });
        confirmBtn.click();
    }

    async queryLossRate(label, container) {
        await listenElement(label, container, () => {
            let span = container.querySelectorAll('div[class="okui-input-box"]')[3]?.querySelector('span[class^="index_textContainer"]');
            if (span && span.textContent === '收益率') {
                return span;
            } else {
                return undefined;
            }
        });
    }

    async inputLossPercent(label, container) {
        let lossInput = await listenElement(label, container, () => container.querySelectorAll('div[class="okui-input-box"]')[3]?.querySelector('input[type="text"]'));
        this.input(lossInput, this.lossPercent);
    }

    async clickProfitDialogPanelConfirm(label, container) {
        let confirmBtn1 = await listenElement(label, container, () => container.nextElementSibling?.querySelector('button[data-testid="okd-dialog-confirm-btn"]'));
        confirmBtn1.click();
    }

    async waitSettingSuccess(label) {
        await listenElement(label, document, () => {
            let scrollBox = document.getElementById("scroll-box");
            return !scrollBox ? document : null;
        });
    }

    async clickSubmit(label, container) {
        let submit = await listenElement(label, container, () => container.querySelector('div[class="submit-btn-box"] span'));
        submit.click();
    }

    async waitSubmitSuccess(label, container, condition) {
        await listenElement(label, container, () => {
            let button = container.querySelector('div[class="submit-btn-box"] span');
            return button?.textContent.startsWith(condition) ? button : null;
        });
    }
}

class MarketGesture extends Gesture {
    constructor(profitPercent, lossPercent) {
        super(profitPercent, lossPercent);
    }

    async apply(callback, label, id, signed) {
        let inputPanel = await this.queryInputPanel('1. 查询输入面板', id);
        await this.clickMarket('2. 点击市价委托', inputPanel);
        await this.clickQuantity('3. 点击数量', inputPanel);
        let checkBoxParent = await this.clickProfitLoss('4. 点击止盈止损', inputPanel);
        await this.clickAdvanceProfit('5. 点击高级', checkBoxParent);
        let dialogPanel = await this.queryProfitDialogPanel('6. 查询止盈止损对话框');
        await this.inputProfitPercent('7. 输入止盈', dialogPanel, this.profitPercent * signed);
        await this.clickLossOption('8. 点击止损选项', dialogPanel);
        let lossDialog = await this.queryLossDialog('9. 查询止损对话框');
        await this.clickLossRate('10. 点击止损收益率', lossDialog);
        await this.clickLossDialogConfirm('11. 点击确定', lossDialog);
        await this.queryLossRate('12. 查询止损收益率', dialogPanel);
        await this.inputLossPercent('13. 止损输入', dialogPanel);
        await this.clickProfitDialogPanelConfirm('14. 点击确定', dialogPanel);
        await this.waitSettingSuccess('15. 等待设置成功');
        await this.clickSubmit('16. 点击' + label, inputPanel);
        await this.waitSubmitSuccess('17. 等待执行成功', inputPanel, label);
        if (callback) {
            callback();
        }
    }

    executeFull(callback) {
        this.apply(callback, '开多', "leftPoForm", 1);
    }

    executeEmpty(callback) {
        this.apply(callback, '开空', "rightPoForm", -1);
    }
}


class DelegateGesture extends Gesture {
    constructor(profitPercent, lossPercent) {
        super(profitPercent, lossPercent);
    }

    async apply(delegatePrice, id, label, signed, callback) {
        let inputPanel = await this.queryInputPanel('1. 查询输入面板', id);
        await this.clickDelegate('2. 点击限价委托', inputPanel);
        await this.inputDelegatePrice('3 输入委托价', inputPanel, delegatePrice);
        await this.clickQuantity('4 点击数量', inputPanel);
        let checkBoxParent = await this.clickProfitLoss('5. 点击止盈止损', inputPanel);
        await this.clickAdvanceProfit('6. 点击高级', checkBoxParent);
        let dialogPanel = await this.queryProfitDialogPanel('7. 查询止盈止损对话框');
        await this.inputProfitPercent('8. 输入止盈', dialogPanel, this.profitPercent * signed);
        await this.clickLossOption('9. 点击止损选项', dialogPanel);
        let lossDialog = await this.queryLossDialog('10. 查询止损对话框');
        await this.clickLossRate('11. 点击止损收益率', lossDialog);
        await this.clickLossDialogConfirm('12. 点击确定', lossDialog);
        await this.queryLossRate('13. 查询止损收益率', dialogPanel);
        await this.inputLossPercent('14. 止损输入', dialogPanel);
        await this.clickProfitDialogPanelConfirm('15. 点击确定', dialogPanel);
        await this.waitSettingSuccess('16. 等待设置成功');
        await this.clickSubmit('17. 点击' + label, inputPanel);
        await this.waitSubmitSuccess('18. 等待执行成功', inputPanel, label);
        if (callback) {
            callback();
        }
    }

    executeFull(delegatePrice, callback) {
        this.apply(delegatePrice, 'leftPoForm', '开多', 1, callback);
    }

    executeEmpty(delegatePrice, callback) {
        this.apply(delegatePrice, 'rightPoForm', '开空', -1, callback);
    }
}
