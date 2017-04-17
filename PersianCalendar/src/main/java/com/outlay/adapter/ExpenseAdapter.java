package com.outlay.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.util.Utils;
import com.byagowi.persiancalendar.view.activity.ConvertPersianCal;
import com.github.johnkil.print.PrintView;

import com.outlay.dao.Expense;
import com.outlay.utils.DateUtils;
import com.outlay.utils.FormatUtils;
import com.outlay.utils.IconUtils;
import com.outlay.utils.NumberTextWatcherForThousand;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import calendar.CivilDate;
import calendar.DateConverter;
import calendar.PersianDate;


/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> items;
    private OnExpenseClickListener onExpenseClickListener;
    private Utils utils;
    public ExpenseAdapter(List<Expense> categories) {
        this.items = categories;
    }

    public ExpenseAdapter() {
        this(new ArrayList<>());
    }

    public void setItems(List<Expense> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setOnExpenseClickListener(OnExpenseClickListener onExpenseClickListener) {
        this.onExpenseClickListener = onExpenseClickListener;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_expense_expense, parent, false);
        final ExpenseViewHolder viewHolder = new ExpenseViewHolder(v);
        utils = Utils.getInstance(parent.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        Expense expense = items.get(position);
        holder.note.setText(expense.getNote());
        holder.root.setOnClickListener(v -> {
            if (onExpenseClickListener != null) {
                onExpenseClickListener.onExpenseClicked(expense);
            }
        });
        holder.categoryAmount.setText(ConvertPersianCal.toPersianNumber(NumberTextWatcherForThousand.getDecimalFormattedString(FormatUtils.formatAmount(expense.getAmount()))));

        Calendar reportAmount = Calendar.getInstance();
        reportAmount.setTime(expense.getReportedAt());

        CivilDate civilDate = new CivilDate(
                reportAmount.get(Calendar.YEAR),
                reportAmount.get(Calendar.MONTH) + 1,
                reportAmount.get(Calendar.DAY_OF_MONTH));
        PersianDate persianDate = DateConverter.civilToPersian(civilDate);
        String pDate = utils.shape(utils.dateToString(persianDate));

        holder.categoryDate.setText(pDate);
        holder.categoryTitle.setText(expense.getCategory().getTitle());
        IconUtils.loadCategoryIcon(expense.getCategory(), holder.categoryIcon);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.categoryNote)
        TextView note;

        @Bind(R.id.expenseContainer)
        View root;

        @Bind(R.id.categoryIcon)
        PrintView categoryIcon;

        @Bind(R.id.categoryTitle)
        TextView categoryTitle;

        @Bind(R.id.categoryDate)
        TextView categoryDate;

        @Bind(R.id.categoryAmount)
        TextView categoryAmount;

        public ExpenseViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public interface OnExpenseClickListener {
        void onExpenseClicked(Expense e);
    }
}
