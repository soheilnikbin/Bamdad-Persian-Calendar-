/*
 * Copyright (C) 2015 Federico Iosue (federico.iosue@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.omninotes.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.SparseArray;
import android.widget.RemoteViews;

import com.byagowi.persiancalendar.R;
import com.omninotes.MainActivityPep;
import com.omninotes.utils.Constants;


@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ListWidgetProvider extends WidgetProvider {

    @Override
    protected RemoteViews getRemoteViews(Context mContext, int widgetId,
                                         boolean isSmall, boolean isSingleLine, 
                                         SparseArray<PendingIntent> pendingIntentsMap) {
        RemoteViews views;
        if (isSmall) {
            views = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_layout_small_pep);
            views.setOnClickPendingIntent(R.id.list,
                    pendingIntentsMap.get(R.id.list));
        } else if (isSingleLine) {
            views = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_layout_pep);
            views.setOnClickPendingIntent(R.id.add,
                    pendingIntentsMap.get(R.id.add));
            views.setOnClickPendingIntent(R.id.list,
                    pendingIntentsMap.get(R.id.list));
            views.setOnClickPendingIntent(R.id.camera,
                    pendingIntentsMap.get(R.id.camera));
        } else {
            views = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_layout_list_pep);
            views.setOnClickPendingIntent(R.id.add,
                    pendingIntentsMap.get(R.id.add));
            views.setOnClickPendingIntent(R.id.list,
                    pendingIntentsMap.get(R.id.list));
            views.setOnClickPendingIntent(R.id.camera,
                    pendingIntentsMap.get(R.id.camera));

            // Set up the intent that starts the ListViewService, which will
            // provide the views for this collection.
            Intent intent = new Intent(mContext, ListWidgetService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            views.setRemoteAdapter(R.id.widget_list, intent);

            Intent clickIntent = new Intent(mContext, MainActivityPep.class);
            clickIntent.setAction(Constants.ACTION_WIDGET);
            PendingIntent clickPI = PendingIntent.getActivity(mContext, 0,
                    clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setPendingIntentTemplate(R.id.widget_list, clickPI);
        }
        return views;
    }


}