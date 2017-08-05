package com.home77.kake.business.home.presenter;

import com.home77.common.base.collection.Params;
import com.home77.common.base.debug.DLog;
import com.home77.common.base.pattern.Instance;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.business.home.model.Photo;
import com.home77.kake.business.home.view.LocalPhotoView;
import com.home77.kake.business.theta.LoadFileListResult;
import com.home77.kake.business.theta.ThetaRequest;
import com.home77.kake.business.theta.ThetaService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CJ
 */
public class LocalPhotoPresenter extends BasePresenter<LocalPhotoView> {

  private static final String TAG = LocalPhotoPresenter.class.getSimpleName();
  private final ThetaService thetaService;

  public LocalPhotoPresenter(LocalPhotoView attachedView) {
    super(attachedView);
    thetaService = Instance.of(ThetaService.class);
  }

  @Override
  public void start() {
  }

  @Override
  public void onViewCreated() {
    thetaService.loadFileList(ThetaRequest.FILE_TYPE_ALL,
                              0,
                              10,
                              0,
                              new ThetaService.OnThetaServiceCallback() {
                                @Override
                                public void onThetaServiceSuccess(final Params params) {
                                  DLog.d(TAG, "onThetaServiceSuccess: %s", params.toString());
                                  LoadFileListResult loadFileListResult =
                                      params.get(ThetaService.PARAMS_KEY_FILE_LIST,
                                                 new LoadFileListResult());
                                  if (loadFileListResult.getTotalEntries() == 0) {
                                    attachedView.onEmpty();
                                  } else {
                                    List<Photo> photoList = new ArrayList<>();
                                    for (LoadFileListResult.EntriesBean bean : loadFileListResult.getEntries()) {
                                      photoList.add(bean.map());
                                    }
                                    attachedView.onIdle(photoList);
                                  }
                                }

                                @Override
                                public void onThetaServiceError(final String msg) {
                                  attachedView.onError();
                                  DLog.d(TAG, "onThetaServiceError: %s", msg);
                                }
                              });
  }

  @Override
  public void onViewDestroy() {

  }
}
