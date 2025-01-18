package com.example.proyectodsa_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodsa_android.models.FAQ;

import java.util.ArrayList;
import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder>{
    private List<FAQ> faqs = new ArrayList<>();

    public void setFaqs(List<FAQ> faqs) {
        this.faqs = faqs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_faq, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQ faq = faqs.get(position);
        holder.bind(faq);
    }

    @Override
    public int getItemCount() {
        return faqs.size();
    }

    static class FAQViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvQuestion;
        private final TextView tvAnswer;
        private final TextView tvDate;

        FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        void bind(FAQ faq) {
            tvQuestion.setText(faq.getQuestion());
            tvAnswer.setText(faq.getAnswer());
            tvDate.setText(faq.getDate());
        }
    }
}
