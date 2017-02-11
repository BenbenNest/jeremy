/**
 * Copyright 2009 Mark Wyszomierski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeremy.lychee.widget.BookReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;


public class ActivityTestMultilineEllipse extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout llMain = new LinearLayout(this);
        llMain.setOrientation(LinearLayout.VERTICAL);
        llMain.setBackgroundColor(0xFFFFFFFF);
        addContentView(llMain, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        ScrollView scroll = new ScrollView(this);
        scroll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        llMain.addView(scroll);
        
        LinearLayout llContent = new LinearLayout(this);
        llContent.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        llContent.setOrientation(LinearLayout.VERTICAL);
        llContent.setBackgroundColor(0xFFFFFFFF);
        scroll.addView(llContent);
        
        // Make one widget that won't ellipsize within three lines.
        TextViewMultilineEllipse tv1 = new TextViewMultilineEllipse(this);
        tv1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        tv1.setEllipsis("...");
        tv1.setEllipsisMore(" Read More!");
        tv1.setText("This is some short text. It won't need to be ellipsized.");
        tv1.setMaxLines(3);
        tv1.setPadding(10, 10, 10, 10);
        tv1.setBackgroundColor(0xFFE4BEF1);
        llContent.addView(tv1);
        
        // Make one widget that is long enough to ellipsize within three lines. Add a click handler,
        // expand and collapse on click.
        final TextViewMultilineEllipse tv2 = new TextViewMultilineEllipse(this);
        tv2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        tv2.setEllipsis("...");
        tv2.setEllipsisMore(" Read More!");
        tv2.setMaxLines(3);
        tv2.setText("This is some longer text. It should wrap and then eventually be ellipsized once it gets way too long for the horizontal width of the current application screen. We should be fixed to max [N] lines height.");
        tv2.setPadding(10, 10, 10, 10);
        tv2.setBackgroundColor(0xFFFCDFB2);
        tv2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (tv2.getIsExpanded()) {
                    tv2.collapse();
                }
                else {
                    tv2.expand();
                }
            }
        });
        llContent.addView(tv2);
    }
}