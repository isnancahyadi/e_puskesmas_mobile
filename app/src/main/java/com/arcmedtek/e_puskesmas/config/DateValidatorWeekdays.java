package com.arcmedtek.e_puskesmas.config;

import android.os.Parcel;

import androidx.annotation.Nullable;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class DateValidatorWeekdays implements CalendarConstraints.DateValidator {

    private Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    public static final Creator<DateValidatorWeekdays> CREATOR = new Creator<DateValidatorWeekdays>() {
        @Override
        public DateValidatorWeekdays createFromParcel(Parcel parcel) {
            return new DateValidatorWeekdays();
        }

        @Override
        public DateValidatorWeekdays[] newArray(int i) {
            return new DateValidatorWeekdays[i];
        }
    };

    @Override
    public boolean isValid(long date) {
        utc.setTimeInMillis(date);
        int dayOfWeek = utc.get(Calendar.DAY_OF_WEEK);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, 8);
        long end = calendar.getTimeInMillis();

        return dayOfWeek != Calendar.SUNDAY && date > MaterialDatePicker.todayInUtcMilliseconds() && date < end;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DateValidatorWeekdays)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        Object[] hashedFields = {};
        return Arrays.hashCode(hashedFields);
    }
}
