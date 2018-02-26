package com.prem.test.swrve.dagger.modules;

import android.content.Context;

import com.prem.test.swrve.utils.annotation.validator.EditTextUrlValidator;
import com.prem.test.swrve.utils.file.FileManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by prem on 25/02/2018.
 */
@Module
public class UtilsModule {

    @Provides
    FileManager provideFileManager(Context context) {
        return new FileManager(context);
    }

    @Provides
    EditTextUrlValidator provideEditTextUrlValidator() {
        return new EditTextUrlValidator();
    }

}
