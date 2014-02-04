package com.android.server.pm;
/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;

/*
 * class xmlCheckExt is trying to check by xml rules definition
 */
public class xmlCheckExt implements ICheckExt {
    final private String TAG = "xmlCheckExt";
    final private String CHECKXMLPATH = "/system/lib/arm/check.xml";
    private HashMap<String,String > mMap = new  HashMap<String,String >();

    public boolean doCheck(String... params) {
        String param = null;
        String param_tag = null;
        InputStream in = null;
        try {
            int eventType;
            String tag;
            if (params.length == 0)
                return false;
            param = params[0];
            param_tag = params[1];
            XmlPullParser xmlParser = Xml.newPullParser();
            File file = new File(CHECKXMLPATH);
            if (!file.exists())
                return false;
            in = new FileInputStream(file);
            xmlParser.setInput(in, "utf-8");

            eventType = xmlParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xmlParser.getName();
                        Log.d(TAG,"<"+tag+">");
                        addTag(tag, xmlParser.nextText());
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xmlParser.getName();
                        Log.d(TAG,"</"+tag+">");
                        break;
                    default:
                        break;
                }
                eventType = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            if (in != null)
                in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return checkPkgName(param, param_tag);
    }

    /*Function:checkTag
     *Description:
     * add tag name to hash map
     *Parameter:
     * tag - tag name in xml
     * text - text for the tag
     *Return:
     * true
     */
    boolean addTag(String tag, String text) {
        String pkgName = text;
        Log.d(TAG, " pkgName = " + pkgName);
        if (!mMap.containsKey(pkgName))
            mMap.put(pkgName,tag);
        return true;
    }

    boolean checkPkgName(String pkgName, String tag) {
        String value = mMap.get(pkgName);
        if (value == null)
            return false;
        else
            return value.equals(tag);
    }

}
