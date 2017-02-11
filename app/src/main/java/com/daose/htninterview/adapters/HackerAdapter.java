package com.daose.htninterview.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daose.htninterview.R;
import com.daose.htninterview.helpers.LetterTileProvider;
import com.daose.htninterview.listeners.OnHackerListClickListener;
import com.daose.htninterview.models.Hacker;
import com.daose.htninterview.models.Skill;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import io.realm.Sort;

public class HackerAdapter extends RealmBasedRecyclerViewAdapter<Hacker, HackerAdapter.ViewHolder> {

    private RealmResults<Hacker> hackers;
    private OnHackerListClickListener listener;
    private Context ctx;

    public HackerAdapter(Context ctx, RealmResults<Hacker> hackers, OnHackerListClickListener listener) {
        super(ctx, hackers, true, true);
        this.ctx = ctx;
        this.hackers = hackers;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hacker, parent, false);
        final ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(hackers.get(vh.getLayoutPosition()));
            }
        });
        return vh;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder holder, int position) {
        Hacker hacker = hackers.get(position);

        TextView name = holder.name;
        name.setText(hacker.getName());

        TextView skills = holder.skills;
        skills.setText(getFormattedSkills(hacker));

        SimpleDraweeView profile = holder.profile;
        profile.getHierarchy().setPlaceholderImage(LetterTileProvider.getTile(ctx, hacker.getName(), R.dimen.item_profile));
        profile.setImageURI(Uri.parse(hacker.getPicture()));
    }

    @Override
    public int getItemCount() {
        return hackers.size();
    }

    private String getFormattedSkills(Hacker hacker) {
        StringBuilder sb = new StringBuilder("");

        int numOfSkills = Math.min(hacker.getSkills().size(), 2);
        List<Skill> skills = hacker.getSkills().sort(Skill.RATING, Sort.DESCENDING);
        String prevSkillName = null;
        int i = 0;
        while(numOfSkills > 0 && i < skills.size()) {
            String skillName = skills.get(i).name;
            if(skillName.equals(prevSkillName)) {
                i++;
            } else {
                sb.append(skillName);
                sb.append(", ");

                prevSkillName = skillName;
                i++;
                numOfSkills--;
            }
        }

        return sb.substring(0, sb.length() - 2);
    }

    public static class ViewHolder extends RealmViewHolder {
        public TextView name;
        public TextView skills;
        public SimpleDraweeView profile;

        public ViewHolder(View itemView){
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            skills = (TextView) itemView.findViewById(R.id.skills);
            profile = (SimpleDraweeView) itemView.findViewById(R.id.profile);
        }
    }
}
