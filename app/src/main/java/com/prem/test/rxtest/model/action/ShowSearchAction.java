package com.prem.test.rxtest.model.action;

/**
 * Created by prem on 20/02/2018.
 */

public class ShowSearchAction extends BaseAction {

    private boolean resetUrlText;

    public ShowSearchAction(boolean resetUrlText) {
        this.resetUrlText = resetUrlText;
    }

    public boolean isResetUrlText() {
        return resetUrlText;
    }
}
