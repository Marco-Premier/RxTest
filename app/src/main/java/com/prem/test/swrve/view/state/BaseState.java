package com.prem.test.swrve.view.state;

/**
 * Created by prem on 14/02/2018.
 */

public abstract class BaseState {

    protected abstract void defaultState();

    public BaseState(){
        this.defaultState();
    }

}
