package com.founder.stickycalendar.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.founder.stickycalendar.R;
import com.founder.stickycalendar.adapter.TopViewPagerAdapter;
import com.founder.stickycalendar.ui.activity.CalendarScrollViewActivity;
import com.founder.stickycalendar.utils.DateBean;
import com.founder.stickycalendar.utils.OtherUtils;
import com.founder.stickycalendar.view.CalendarView;
import com.founder.stickycalendar.view.ContainerLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <P>Title:</P>
 * <P>Description::</P>
 *
 * @author liu-yuwu
 * @date 16-5-19
 */
public class CalendarListViewFragment extends Fragment {


    private ContainerLayout containerView;
    private TextView txToday;
    private ViewPager vpCalender;
    private ListView viewContent;

    private List<View> calenderViews = new ArrayList<>();

    /**
     * 日历向左或向右可翻动的天数
     */
    private int INIT_PAGER_INDEX = 120;

    public static CalendarListViewFragment newInstance(int type) {
        Bundle args = new Bundle();
        CalendarListViewFragment fragment = new CalendarListViewFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArguments().getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_listview, null);


        containerView = (ContainerLayout) view.findViewById(R.id.container);
        txToday = (TextView) view.findViewById(R.id.tx_today);
        vpCalender = (ViewPager) view.findViewById(R.id.vp_calender);
        viewContent = (ListView) view.findViewById(R.id.view_content);


        initCalendar();

        String[] strs = new String[100];
        for (int i = 0; i < strs.length; i++) {
            strs[i] = String.format("第%d行", i);
        }
        viewContent.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, strs));
        viewContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getContext(), CalendarScrollViewActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }

    private void initCalendar() {
        Calendar calendar = Calendar.getInstance();
        txToday.setText(OtherUtils.formatDate(calendar.getTime()));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < 3; i++) {
            CalendarView calendarView = new CalendarView(getContext(), i, year, month);
            calendarView.setOnCalendarClickListener(new OnMyCalendarClickerListener());
            if (i == 0) {
                containerView.setRowNum(calendarView.getColorDataPosition() / 7);
            }
            calenderViews.add(calendarView);
        }
        final TopViewPagerAdapter adapter = new TopViewPagerAdapter(getContext(), calenderViews, INIT_PAGER_INDEX, calendar);
        vpCalender.setAdapter(adapter);
        vpCalender.setCurrentItem(INIT_PAGER_INDEX);
        vpCalender.addOnPageChangeListener(new OnMyViewPageChangeListener());


        vpCalender.post(new Runnable() {
            @Override
            public void run() {
                initEventDays((CalendarView) adapter.getChildView(0));
            }
        });
    }

    /**
     * @param calendarView
     */
    private void initEventDays(CalendarView calendarView) {
        //设置含有事件的日期 1-9号
        List<String> eventDays = new ArrayList<>();//根据实际情况调整，传入时间格式(yyyy-MM)
        for (int j = 0; j < 10; j++) {
            eventDays.add(calendarView.getCurrentDay() + "-0" + j);
        }
        calendarView.setEventDays(eventDays);
    }

    /**
     * 日期滚动时回调
     */
    class OnMyViewPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {

            CalendarView calendarView = (CalendarView) calenderViews.get(position % 3);
            txToday.setText(calendarView.getCurrentDay());
            containerView.setRowNum(0);
            calendarView.initFirstDayPosition(0);

            //设置含有事件的日期 1-9号
            initEventDays(calendarView);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 点击某个日期回调
     */
    class OnMyCalendarClickerListener implements CalendarView.OnCalendarClickListener {
        @Override
        public void onCalendarClick(int position, DateBean dateBean) {
            txToday.setText(OtherUtils.formatDate(dateBean.getDate()));
        }
    }

}
