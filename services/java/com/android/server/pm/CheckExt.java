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

import java.util.List;
import java.util.ArrayList;

/*
 * class CheckExt is trying to check params
 */
public class CheckExt implements ICheckExt {
    final private String TAG = "CheckExt";
    private List<ICheckExt> checklist;

    public CheckExt() {
        checklist = new ArrayList<ICheckExt>();
        ICheckExt check = new xmlCheckExt();
        checklist.add(check);
    }

    public boolean doCheck(String... params) {
        if (checklist.size() == 0)
            return false;
        ICheckExt check;
        for (int i = 0; i < checklist.size(); i++) {
            check = checklist.get(i);
            if (check.doCheck(params))
                return true;

        }
        return false;
    }
}
