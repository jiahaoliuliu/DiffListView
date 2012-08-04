/*
 * Copyright (C) 2012 Jiahao Liu <http://www.jiahaoliuliu.com>
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

package com.jiahaoliuliu.android.difflistview;

import java.util.ArrayList;
import java.util.TreeSet;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DiffListView extends ListActivity {

    private static final String LOG_TAG = DiffListView.class.getSimpleName();
    private static final int MAX_NUMBER_CONTENT = 100;
    private MyCustomAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MyCustomAdapter();
        for (int i = 1; i < MAX_NUMBER_CONTENT; i++) {
            mAdapter.addItem("item " + i);
            if (i % 4 == 0) {
                mAdapter.addSeparatorItem("separator " + i/4);
            }
        }
        setListAdapter(mAdapter);
    }

    private class MyCustomAdapter extends BaseAdapter {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;
        private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

        private ArrayList<String> mData = new ArrayList<String>();
        private LayoutInflater mInflater;

        private TreeSet<Integer> mSeparatorSet = new TreeSet<Integer>();

        public MyCustomAdapter() {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final String item) {
            mData.add(item);
            // The notification is not necessary since the items are not added dynamically
            //notifyDataSetChanged();
        }

        public void addSeparatorItem(final String item) {
            mData.add(item);
            // Save separator position
            // This is used to check whether the element is a separator or an item
            mSeparatorSet.add(mData.size() - 1);
            // The notification is not necessary since the separators are not added dynamically
            //notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return mSeparatorSet.contains(position) ? TYPE_SEPARATOR: TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);
            Log.v(LOG_TAG, "getView " + position + " " + convertView + " type = " + type);
            if (convertView == null) {
                holder = new ViewHolder();
                switch (type) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.item,  null);
                    holder.textView = (TextView)convertView.findViewById(R.id.text);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.separator, null);
                    holder.textView = (TextView)convertView.findViewById(R.id.textSeparator);
                    break;
                default:
                    break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.textView.setText(mData.get(position));
            return convertView;
        }
    }

    public static class ViewHolder {
        public TextView textView;
    }
}
