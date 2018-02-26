package com.prem.test.swrve.presenter;

import com.prem.test.swrve.Swrve;
import com.prem.test.swrve.model.BaseModel;
import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.action.DownloadImageAction;
import com.prem.test.swrve.model.persistent.dao.SearchHistoryDao;
import com.prem.test.swrve.model.persistent.realm.SearchHistoryWrapper;
import com.prem.test.swrve.presenter.binders.BindDownloadImageState;
import com.prem.test.swrve.presenter.contract.ActionBasePresenter;
import com.prem.test.swrve.view.contract.UrlListView;
import com.prem.test.swrve.view.event.ImagesListItemTapEvent;
import com.prem.test.swrve.view.event.UiEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by prem on 18/02/2018.
 */

public class UrlListPresenter extends ActionBasePresenter<UrlListView> {

    @Inject
    @Named("DownloadImageModel")
    BaseModel model;

    @Inject
    BindDownloadImageState stateBinder;

    private CompositeDisposable disposables = new CompositeDisposable();
    private PublishSubject<ImagesListItemTapEvent> itemViewClickSubject;
    private boolean isRecyclerViewSetup = false;
    private Disposable disposableModel;
    private Disposable disposableSearchHistoryStore;

    public UrlListPresenter(PublishSubject<ImagesListItemTapEvent> itemViewClickSubject){
        this.itemViewClickSubject = itemViewClickSubject;
        Swrve.getDefaultComponent().inject(this);
    }

    @Override
    public void attachView(UrlListView view){
        super.attachView(view);

        if(!isRecyclerViewSetup){
            isRecyclerViewSetup = true;
            ifViewAttached(ui -> ui.setupRecyclerView(stateBinder.getState()));
        }

        model.setup(setupActions());
        disposableModel = stateBinder.bindState(model.getResults()).debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downloadImageState -> {
                    ifViewAttached(ui -> ui.refreshState(downloadImageState));
                });

        disposableSearchHistoryStore = SearchHistoryDao
                .getSearchHistoryStore()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(store -> {
                    if(store.size() > 0)
                        ifViewAttached(ui -> ui.refreshStore(store));
                    else
                        ifViewAttached(ui -> ui.showEmptyMessage());
                });

        disposables.add(disposableModel);
        disposables.add(disposableSearchHistoryStore);

    }

    @Override
    public void detachView(){
        super.detachView();

        disposables.delete(disposableModel);
        disposables.delete(disposableSearchHistoryStore);

    }

    @Override
    protected Observable<BaseAction> setupActions(){
        return itemViewClickSubject.compose(new ObservableTransformer<UiEvent, BaseAction>(){

            @Override
            public ObservableSource<BaseAction> apply(Observable<UiEvent> upstream) {
                return upstream.map(new Function<UiEvent, BaseAction>() {
                    @Override
                    public BaseAction apply(UiEvent uiEvent) throws Exception {
                        if(uiEvent instanceof ImagesListItemTapEvent){
                            final long idUrl = ((ImagesListItemTapEvent)uiEvent).getIdUrl();
                            SearchHistoryWrapper searchHistoryWrapper = SearchHistoryDao.findUrlById(idUrl);
                            return new DownloadImageAction(searchHistoryWrapper.getUrl(),idUrl);
                        }
                        throw new IllegalArgumentException("unknown event");
                    }
                });
            }});
    }

}
