package com.prem.test.rxtest.dagger.modules;

import android.content.Context;

import com.prem.test.rxtest.utils.annotation.validator.EditTextUrlValidator;
import com.prem.test.rxtest.utils.file.ImageDownloader;

import dagger.Module;
import dagger.Provides;

/**
 * Created by prem on 25/02/2018.
 */
@Module
public class UtilsModule {

    @Provides
    ImageDownloader provideFileManager(Context context) {
        return new ImageDownloader(context);
    }

    @Provides
    EditTextUrlValidator provideEditTextUrlValidator() {
        return new EditTextUrlValidator();
    }

}
