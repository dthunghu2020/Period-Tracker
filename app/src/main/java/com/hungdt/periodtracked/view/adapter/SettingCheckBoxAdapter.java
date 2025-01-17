package com.hungdt.periodtracked.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hungdt.periodtracked.R;
import com.hungdt.periodtracked.model.CalendarPick;
import com.hungdt.periodtracked.model.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.hungdt.periodtracked.view.SettingPeriodActivity.beginCycleDayAdapter;

public class SettingCheckBoxAdapter extends RecyclerView.Adapter<SettingCheckBoxAdapter.SettingCheckBoxHolder> {
    private OnCalendarSettingListener onCalendarSettingListener;
    List<CalendarPick> settingPeriods;
    LayoutInflater layoutInflater;
    Calendar calendar = Calendar.getInstance();
    Calendar calendarInstance = Calendar.getInstance();
    String monthOfYear;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
    SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
    SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

    public SettingCheckBoxAdapter(Context context, List<CalendarPick> settingPeriods, String monthOfYear) {
        this.settingPeriods = settingPeriods;
        this.monthOfYear = monthOfYear;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SettingCheckBoxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SettingCheckBoxHolder(layoutInflater.inflate(R.layout.item_checkbox_setting, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SettingCheckBoxHolder holder, final int position) {
        CalendarPick settingPeriod = settingPeriods.get(position);
        calendar.setTime(settingPeriod.getDate());

        Date date = null;
        try {
            date = dateFormat.parse(monthOfYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int currentMonth = Integer.parseInt(sdfMonth.format(date));
        int currentYear = Integer.parseInt(sdfYear.format(date));

        int instanceDay = calendarInstance.get(Calendar.DAY_OF_MONTH);
        int instanceMonth = calendarInstance.get(Calendar.MONTH) + 1;
        int instanceYear = calendarInstance.get(Calendar.YEAR);

        final int displayDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int displayMonth = calendar.get(Calendar.MONTH) + 1;
        final int displayYear = calendar.get(Calendar.YEAR);

        holder.txtDay.setText("" + displayDay);
        final String curDay;
        String day;
        String month;
        if (displayDay < 10) {
            day = "0" + displayDay;
        } else {
            day = "" + displayDay;
        }
        if (displayMonth < 10) {
            month = "0" + displayMonth;
        } else {
            month = "" + displayMonth;
        }
        curDay = day + "-" + month + "-" + displayYear;
        if (beginCycleDayAdapter.equals(curDay)) {
            holder.checkBox.setChecked(true);
        }


        if (countNumberDay(displayYear, displayMonth, displayDay) > countNumberDay(instanceYear, instanceMonth, instanceDay)) {
            holder.txtDay.setTextColor(layoutInflater.getContext().getResources().getColor(R.color.gray));
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        if (displayDay == instanceDay && displayMonth == instanceMonth && displayYear == instanceYear) {
            holder.txtDay.setTextColor(layoutInflater.getContext().getResources().getColor(R.color.red));
            holder.txtDay.setTypeface(holder.txtDay.getTypeface(), Typeface.BOLD);
        }

        if (displayMonth != currentMonth || displayYear != currentYear) {
            holder.itemView.setVisibility(View.GONE);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCalendarSettingListener.OnItemClicked(position, curDay);
                //Toast.makeText(layoutInflater.getContext(), displayDay + "-" + displayMonth + "-" + displayYear, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingPeriods.size();
    }

    static class SettingCheckBoxHolder extends RecyclerView.ViewHolder {
        private TextView txtDay;
        private CheckBox checkBox;

        public SettingCheckBoxHolder(@NonNull View itemView) {
            super(itemView);
            txtDay = itemView.findViewById(R.id.txtDay);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public void setOnCalendarSettingListener(OnCalendarSettingListener onCalendarSettingListener) {
        this.onCalendarSettingListener = onCalendarSettingListener;
    }

    public interface OnCalendarSettingListener {
        void OnItemClicked(int position, String day);
    }

    private int countNumberDay(int year, int month, int day) {
        if (month < 3) {
            year--;
            month += 12;
        }
        return 365 * year + year / 4 - year / 100 + year / 400 + (153 * month - 457) / 5 + day - 306;
    }

}
