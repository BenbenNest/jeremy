package com.jeremy.lychee.adapter.news;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.news.Comment;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.widget.CmtItemLayout;
import com.jeremy.lychee.widget.LinearLayoutForRecyclerView;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ImageOptiUrl;
import com.jeremy.lychee.utils.ToastHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Comment> mComments;
    List<Comment> hotComments;
    private Context mContext;

    private OnLoadMoreListener mOnLoadMoreListener;
    private Animation mClickAnim;
    private int mSize;
    private boolean loadFail;
    private boolean isOver;
    private OnReplayListener mOnReplayListener;
    private boolean hasHotLayout;

    public CommentAdapter(Context context, List<Comment> mComments, OnReplayListener mOnReplayListener, boolean hasHotLayout) {
        mContext = context;
        this.mComments = mComments;
        this.mOnReplayListener = mOnReplayListener;
        mClickAnim = AnimationUtils.loadAnimation(mContext, com.jeremy.lychee.R.anim.digg_click_scale);
        this.hasHotLayout = hasHotLayout;
    }

    public int getSize() {
        if (mComments != null) {
            return mComments.size();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (mComments != null && mComments.size() > 0) {
            boolean isOver = isOver();
            size = mComments.size();
            if (hasHotLayout) {
                //添加热评header
                size++;
            }
            if (!isOver) {
                //还有更多评论数据
                size++;
            }
        }
        return size;
    }

    @Override
    public int getItemViewType(int position) {
        int itemType = super.getItemViewType(position);
        if (hasHotLayout && position == 0) {
            //热评header
            itemType = 3;
        }
        //评论内容item
        //底部加载bar
        if (!isOver && position == getItemCount() - 1) {
            if (loadFail) {
                itemType = 2;
            } else {
                itemType = 1;
            }
        }
        return itemType;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position >= getItemCount() || viewHolder == null) {
            return;
        }
        final CommentViewHolder holder = (CommentViewHolder) viewHolder;

        int itemType = getItemViewType(position);
        if (itemType == 2) {
            //加载失败bar
            return;
        }
        if (itemType == 3) {
            //热评header
            View hot_cmt_header = holder.itemView;
            if (hasHotComments()) {
                hot_cmt_header.findViewById(com.jeremy.lychee.R.id.hot_cmt_layout).setVisibility(View.VISIBLE);
                LinearLayoutForRecyclerView mLinearLayoutForRecyclerView = (LinearLayoutForRecyclerView) hot_cmt_header.findViewById(com.jeremy.lychee.R.id.linearlayout_recyclerview);
                CommentAdapter adapter = new CommentAdapter(mContext, hotComments, mOnReplayListener, false);
                adapter.setOver(true);
                mLinearLayoutForRecyclerView.setAdapter(adapter);
            } else {
                hot_cmt_header.findViewById(com.jeremy.lychee.R.id.hot_cmt_layout).setVisibility(View.GONE);
            }
            return;
        }
        if (itemType == 1) {
            //loading more
            if (mOnLoadMoreListener != null) {
                int size = mComments.size();
                if (mSize != size) {
                    mSize = size;
                    mOnLoadMoreListener.onLoadMore(mComments.size() + "");
                }
            }

            return;
        }

        if (hasHotLayout) {
            position--;
        }
        if (position < 0 || position >= mComments.size()) {
            return;
        }
        //评论item
        final Comment comment = mComments.get(position);
        holder.comment = comment;
        String user_info = comment.getUser_info();
        String img_url = null;
        String name = null;
        try {
            JSONObject jsonObj = new JSONObject(user_info);
            String user_name = jsonObj.getString("user_name");
            String nick_name = jsonObj.getString("nick_name");
            name = TextUtils.isEmpty(nick_name) ? user_name : nick_name;
            img_url = jsonObj.getString("img_url");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Glide.with(mContext)
                .load(ImageOptiUrl.get(img_url, holder.avata))
                .centerCrop()
                .crossFade()
                .error(com.jeremy.lychee.R.drawable.comment_avata)
                .into(holder.avata);

        holder.account.setText(name);
        holder.commentNum.setText(comment.getLikes());
        if (comment.getDiggok() == 1) {
            holder.commentNum.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    com.jeremy.lychee.R.drawable.comment_plus_done, 0);
            holder.commentNum.setTextColor(mContext.getResources().getColor(com.jeremy.lychee.R.color.praised_color));
        } else {
            holder.commentNum.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.jeremy.lychee.R.drawable.comment_plus, 0);

            holder.commentNum.setTextColor(mContext.getResources().getColor(com.jeremy.lychee.R.color.gray_text));
        }

        holder.avata.setOnClickListener(v ->
                WeMediaChannelActivity.startActivity(mContext, comment.getUid()));
        holder.commentNum.setOnClickListener(v -> {
            TextView num = ((TextView) v);
            if (comment.getDiggok() != 1) {
                if (mOnReplayListener != null) {
                    mOnReplayListener.onDiggClick(comment.getId());
                }
                int diggNum = Integer.valueOf(comment.getLikes());
                comment.setLikes(String.valueOf(diggNum + 1));
                comment.setDiggok(1);
                num.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        com.jeremy.lychee.R.drawable.comment_plus_done, 0);
                num.setTextColor(mContext.getResources().getColor(com.jeremy.lychee.R.color.praised_color));
                num.setText(comment.getLikes());
            } else {
                toast(com.jeremy.lychee.R.string.comment_digged);
            }
            num.startAnimation(mClickAnim);
        });

        String time = comment.getPdate();
        String comment_time = AppUtil.formatTime(time);
        holder.comment_time.setText(comment_time);
        holder.commentContent.setText(comment.getMessage());

        OnClickListener mOnClickListener = v -> {
            final String content = holder.commentContent.getText().toString();
            View cmt_pop_view = View.inflate(mContext, com.jeremy.lychee.R.layout.cmt_pop_view, null);
            final PopupWindow popupWindow = new PopupWindow(cmt_pop_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            cmt_pop_view.setOnClickListener(v1 -> {
                popupWindow.dismiss();
                copy(content);
                toast(com.jeremy.lychee.R.string.copy_ok);
            });

            OnClickListener mOnClickListener1 = v1 -> {
                popupWindow.dismiss();
                switch (v1.getId()) {
                    case com.jeremy.lychee.R.id.replay:
                        if (mOnReplayListener != null) {
                            List<Comment> chain_comments = holder.comment.getSub_comment();
                            mOnReplayListener.onReplay(chain_comments, holder.comment);
                        }
                        break;
                    case com.jeremy.lychee.R.id.copy:
                        copy(content);
                        toast(com.jeremy.lychee.R.string.copy_ok);
                        break;
                }
            };

            float lastX = v.getWidth() / 2;
            CmtItemLayout view = (CmtItemLayout) holder.itemView;
            float lastY = view.getLastY();

            int vH = v.getHeight();
            int pW = mContext.getResources().getDimensionPixelSize(com.jeremy.lychee.R.dimen.pop_width);

            View replay_view = cmt_pop_view.findViewById(com.jeremy.lychee.R.id.replay);
            //隐藏回复功能
            if (isMe(holder.comment.getUid())||true) {
                replay_view.setVisibility(View.GONE);
                pW /= 2;
            }
            replay_view.setOnClickListener(mOnClickListener1);
            cmt_pop_view.findViewById(com.jeremy.lychee.R.id.copy).setOnClickListener(mOnClickListener1);

            int left = (int) (lastX - pW);
            int pH = mContext.getResources().getDimensionPixelSize(com.jeremy.lychee.R.dimen.pop_height);
            int top = (int) (lastY - vH - pH);
            popupWindow.showAsDropDown(v, left, top);
        };

        List<Comment> chain_comments = comment.getSub_comment();
        if (chain_comments != null && chain_comments.size() > 0) {
//            ChainCommentAdapter chain_adapter = new ChainCommentAdapter(mContext, comment.getChain_comments(), mOnReplayListener, holder.linearlayout_recyclerview);
//            holder.linearlayout_recyclerview.setAdapter(chain_adapter);
//            holder.linearlayout_recyclerview.setVisibility(View.VISIBLE);
//
//            holder.itemView.setClickable(false);
//            holder.commentContent.setOnClickListener(mOnClickListener);
        } else {
            holder.linearlayout_recyclerview.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(mOnClickListener);
            holder.commentContent.setClickable(false);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup group, int itemType) {
        switch (itemType) {
            case 3:
                View hot_cmt_header = View.inflate(mContext, com.jeremy.lychee.R.layout.hot_cmt_header, null);
                return new CommentViewHolder(hot_cmt_header);

            case 2:
                View row_loadmore_failed = View.inflate(mContext, com.jeremy.lychee.R.layout.row_loadmore_failed, null);
                row_loadmore_failed.setOnClickListener(v -> {
                    if (mOnLoadMoreListener != null) {
                        int size = mComments.size();
                        if (mSize != size) {
                            mSize = size;
                            mOnLoadMoreListener.onLoadMore(String.valueOf(size));
                            loadFail = false;
                            notifyDataSetChanged();
                        }
                    }
                });
                return new CommentViewHolder(row_loadmore_failed);

            case 1:
                View row_loadmore = View.inflate(mContext, com.jeremy.lychee.R.layout.row_loadmore, null);
                return new CommentViewHolder(row_loadmore);

            default:
                CmtItemLayout view = (CmtItemLayout) View.inflate(mContext, com.jeremy.lychee.R.layout.row_comment, null);
                CommentViewHolder holder = new CommentViewHolder(view);
                holder.avata = (ImageView) view.findViewById(com.jeremy.lychee.R.id.comment_avata);
                holder.account = (TextView) view.findViewById(com.jeremy.lychee.R.id.comment_account);
                holder.comment_time = (TextView) view.findViewById(com.jeremy.lychee.R.id.comment_time);
                holder.commentNum = (TextView) view.findViewById(com.jeremy.lychee.R.id.comment_plus_num);
                holder.commentContent = (TextView) view.findViewById(com.jeremy.lychee.R.id.comment_content);
                holder.linearlayout_recyclerview = (LinearLayoutForRecyclerView) view.findViewById(com.jeremy.lychee.R.id.linearlayout_recyclerview);

                return holder;
        }

    }

    private boolean isOver() {
        return isOver;
    }

    public void setOver(boolean isOver) {
        this.isOver = isOver;
    }

    public boolean hasHotComments() {
        return hotComments!=null&&hotComments.size()>0;
    }

    public void setLoadFail(boolean loadFail) {
        this.loadFail = loadFail;
        mSize = 0;
        notifyDataSetChanged();
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView avata;
        TextView account;
        TextView commentNum;
        TextView comment_time;
        TextView commentContent;
        Comment comment;
        LinearLayoutForRecyclerView linearlayout_recyclerview;

        public CommentViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(String pointer);
    }

    public void setCommentResponse(List<Comment> mComments) {
        this.mComments = mComments;
        mSize = 0;
    }
    public void setHotComment(List<Comment> hotComments) {
        this.hotComments = hotComments;
    }

    public void addCommentResponse(List<Comment> mComments) {
        if (this.mComments == null) {
            this.mComments = mComments;
        } else {
            if (mComments != null) {
                this.mComments.addAll(mComments);
            }
        }
    }

    public void insertComment(Comment comment) {
        if (mComments == null) {
            mComments = new ArrayList<>();
        }
        mComments.add(0, comment);
    }

    public interface OnReplayListener {
        void onReplay(List<Comment> chain_comments, Comment comment);

        void onDiggClick(String cid);
    }

    private void toast(int res_id) {
        ToastHelper.getInstance(mContext).toast(res_id);
    }


    private boolean isMe(String comment_uid) {
        User user = Session.getSession();
        if (user != null && !TextUtils.isEmpty(comment_uid)) {
            String uid = user.getReal_uid();
            return comment_uid.equals(uid);
        }
        return false;
    }

    private void copy(String content) {
        AppUtil.copy(mContext, content);
    }

}
