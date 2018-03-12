package com.prem.test.rxtest.utils.annotation.processor;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.prem.test.rxtest.utils.annotation.Font;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by prem on 13/02/2018.
 */

public class FontInjector {

    public static void injectFont(Object target, Context context){

        Class targetClass = target.getClass();

        //Retrieve all the fields
        Field[] fields = targetClass.getFields();

        //Iterate through fields to check if there are annotated field
        for(Field field : fields){

            Font annotation = field.getAnnotation(Font.class);
            if(null != annotation){

                try {

                    //Retrieve the field type
                    Class<?> tType = field.getType();
                    //Retrieve the method
                    Method setTypeface = tType.getMethod("setTypeface", Typeface.class);
                    //Set the proper font according to the annotation value
                    Typeface typeface = null;
                    switch(annotation.value()){

                        case ROMAN:
                            typeface = Typeface.createFromAsset(context.getAssets(),"AvenirLTStd-Roman.otf");
                            break;
                        case BOOK:
                            typeface = Typeface.createFromAsset(context.getAssets(),"AvenirLTStd-Book.otf");
                            break;
                        case LIGHT:
                            typeface = Typeface.createFromAsset(context.getAssets(),"AvenirLTStd-Light.otf");
                            break;
                        default:
                            //set roman as default
                            typeface = Typeface.createFromAsset(context.getAssets(),"AvenirLTStd-Roman.otf");
                            break;

                    }

                    TextView textView = (TextView)field.get(target);
                    setTypeface.invoke(textView, typeface);

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}
