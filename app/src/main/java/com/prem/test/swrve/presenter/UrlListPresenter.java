package com.prem.test.swrve.presenter;

import com.prem.test.swrve.Swrve;
import com.prem.test.swrve.model.DownloadImageModel;
import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.action.DownloadImageAction;
import com.prem.test.swrve.model.persistent.dao.DownloadImageDao;
import com.prem.test.swrve.model.persistent.dao.SearchHistoryDao;
import com.prem.test.swrve.model.persistent.state.DownloadImageState;
import com.prem.test.swrve.model.persistent.store.SearchHistoryStore;
import com.prem.test.swrve.view.contract.UrlListView;
import com.prem.test.swrve.view.event.ImagesListItemTapEvent;
import com.prem.test.swrve.view.event.UiEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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

public class UrlListPresenter extends BasePresenter<UrlListView> {

    @Inject DownloadImageModel model;
    @Inject
    DownloadImageState state;
    private CompositeDisposable disposables = new CompositeDisposable();
    private PublishSubject<ImagesListItemTapEvent> itemViewClickSubject;
    private boolean isRecyclerViewSetup = false;
    private Disposable disposableModel;
    private Disposable disposableDownloadImageState;
    private Disposable disposableSearchHistoryStore;

    public UrlListPresenter(PublishSubject<ImagesListItemTapEvent> itemViewClickSubject){
        this.itemViewClickSubject = itemViewClickSubject;
        //this.model = new DownloadImageModel();
        Swrve.getModelComponent().inject(this);
        Long ss = state.getCurrentIdUrl();
        boolean aa = state.isShowEtError();
        boolean cc = aa;
    }

    @Override
    public void attachView(UrlListView view){
        super.attachView(view);

        if(!isRecyclerViewSetup){
            isRecyclerViewSetup = true;
            ifViewAttached(ui -> ui.setupRecyclerView(DownloadImageDao.defaultState()));
        }
        disposableModel = model.subscribe(getActions());
        disposableDownloadImageState = DownloadImageDao
                .getDownloadImageState()
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(state -> {
                    ifViewAttached(ui -> ui.refreshState(state));
                });
        disposableSearchHistoryStore = SearchHistoryDao
                .getSearchHistoryStore()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(SearchHistoryDao.findAll())
                .subscribe(store -> {
                    if(store.size() > 0)
                        ifViewAttached(ui -> ui.refreshStore(store));
                    else
                        ifViewAttached(ui -> ui.showEmptyMessage());
                });

        disposables.add(disposableModel);
        disposables.add(disposableDownloadImageState);
        disposables.add(disposableSearchHistoryStore);

    }

    @Override
    public void detachView(){
        super.detachView();

        disposables.delete(disposableModel);
        disposables.delete(disposableDownloadImageState);
        disposables.delete(disposableSearchHistoryStore);

    }

    private Observable<BaseAction> getActions(){
        return itemViewClickSubject.compose(new ObservableTransformer<UiEvent, BaseAction>(){

            @Override
            public ObservableSource<BaseAction> apply(Observable<UiEvent> upstream) {
                return upstream.map(new Function<UiEvent, BaseAction>() {
                    @Override
                    public BaseAction apply(UiEvent uiEvent) throws Exception {
                        if(uiEvent instanceof ImagesListItemTapEvent){
                            final long idUrl = ((ImagesListItemTapEvent)uiEvent).getIdUrl();
                            SearchHistoryStore searchHistoryStore = SearchHistoryDao.findUrlById(idUrl);
                            return new DownloadImageAction(searchHistoryStore.getUrl(),idUrl);
                        }
                        throw new IllegalArgumentException("unknown event");
                    }
                });
            }});
    }

}
