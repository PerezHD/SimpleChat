/*
 * Copyright (C) 2014 Harry Devane
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
package com.battlepvp.simplechat.filter;

import com.battlepvp.simplechat.SimpleChat;
import com.battlepvp.simplechat.filter.filters.FilterAdvertisingIPAddress;
import com.battlepvp.simplechat.filter.filters.FilterAdvertisingURL;
import com.battlepvp.simplechat.filter.filters.FilterForceFirstLetterToUppercase;
import com.battlepvp.simplechat.filter.filters.FilterMaxUppercaseLetters;
import com.battlepvp.simplechat.filter.filters.FilterNoChatUntilMoved;
import com.battlepvp.simplechat.filter.filters.FilterNoDuplicateMessages;
import com.battlepvp.simplechat.filter.filters.FilterNoSwear;
import com.battlepvp.simplechat.filter.filters.FilterRateLimit;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public class FilterRegistry {

      private final SimpleChat pluginInstance;
      @Getter
      // This is an arraylist to retain order.
      private final CopyOnWriteArrayList<Filter> filters;

      public FilterRegistry(SimpleChat pluginInstance) {
            this.pluginInstance = pluginInstance;
            this.filters = new CopyOnWriteArrayList<>();
      }

      public void loadFilters() {
            this.filters.clear();

            this.filters.add(new FilterRateLimit());
            this.filters.add(new FilterNoChatUntilMoved());
            this.filters.add(new FilterNoDuplicateMessages());
            this.filters.add(new FilterAdvertisingIPAddress());
            this.filters.add(new FilterAdvertisingURL());
            this.filters.add(new FilterNoSwear());
            this.filters.add(new FilterMaxUppercaseLetters());
            this.filters.add(new FilterForceFirstLetterToUppercase());
      }

}
