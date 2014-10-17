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
package com.battlepvp.simplechat.utils;

import java.util.concurrent.TimeUnit;

/**
 * https://www.github.com/Harry5573
 *
 * @author Harry5573OP
 */
public class TimeUtils {

      public static String getCountdownStringFromLong(long time) {
            int day = (int) TimeUnit.MILLISECONDS.toDays(time);
            long hours = TimeUnit.MILLISECONDS.toHours(time) - (day * 24);
            long minute = TimeUnit.MILLISECONDS.toMinutes(time) - (TimeUnit.MILLISECONDS.toHours(time) * 60);
            long second = TimeUnit.MILLISECONDS.toSeconds(time) - (TimeUnit.MILLISECONDS.toMinutes(time) * 60);
            return hours + "h " + minute + "m " + second + "s";
      }

      public static String toPrettyFromSeconds(int seconds) {
            if (seconds <= 60) {
                  return seconds + " seconds";
            } else if (seconds > 60 && seconds <= 86400) {
                  return String.valueOf(TimeUnit.SECONDS.toHours(seconds) + " hours");
            } else {
                  return String.valueOf(TimeUnit.SECONDS.toDays(seconds) + " days");
            }
      }

}
