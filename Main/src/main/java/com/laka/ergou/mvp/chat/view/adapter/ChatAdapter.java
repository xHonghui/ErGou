package com.laka.ergou.mvp.chat.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.laka.androidlib.eventbus.EventBusManager;
import com.laka.androidlib.util.GlideUtil;
import com.laka.ergou.R;
import com.laka.ergou.mvp.chat.constant.ChatConstant;
import com.laka.ergou.mvp.chat.constant.ChatEventConstant;
import com.laka.ergou.mvp.chat.constant.MessageConstant;
import com.laka.ergou.mvp.chat.helper.ImageMessageHelper;
import com.laka.ergou.mvp.chat.helper.LinkMessageHelper;
import com.laka.ergou.mvp.chat.helper.TextMessageHelper;
import com.laka.ergou.mvp.chat.helper.VideoMessageHelper;
import com.laka.ergou.mvp.chat.model.bean.ChatRobotInfo;
import com.laka.ergou.mvp.chat.model.bean.ChatUserInfo;
import com.laka.ergou.mvp.chat.model.bean.LinkMessage;
import com.laka.ergou.mvp.chat.model.bean.Message;
import com.laka.ergou.mvp.chat.presenter.ChatPresenter;
import com.laka.ergou.mvp.chat.utils.TimeUtils;
import com.laka.ergou.mvp.main.HomeModuleNavigator;
import com.laka.ergou.mvp.main.RouterNavigator;
import com.laka.ergou.mvp.main.constant.HomeConstant;
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant;
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator;
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant;
import com.laka.ergou.mvp.shopping.ShoppingModuleNavigator;
import com.laka.ergou.mvp.shopping.search.constant.ShoppingSearchConstant;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;

import java.util.HashMap;
import java.util.List;

/**
 * @创建者 CSDN_LQR
 * @描述 会话界面的消息列表适配器
 */
public class ChatAdapter extends LQRAdapterForRecyclerView<Message> {

    private Context mContext;
    private List<Message> mData;
    private ChatPresenter mPresenter;
    private RecyclerView mRvMsg;
    private int mAllItemHeight;

    private static final int SEND_TEXT = R.layout.item_text_send;
    private static final int RECEIVE_TEXT = R.layout.item_text_receive;
    private static final int SEND_IMAGE = R.layout.item_image_send;
    private static final int RECEIVE_IMAGE = R.layout.item_image_receive;
    // 链接发送
    private static final int RECEIVE_LINK = R.layout.item_link_receive;
    private static final int SEND_LINK = R.layout.item_link_send;
    private static final int SEND_STICKER = R.layout.item_sticker_send;
    private static final int RECEIVE_STICKER = R.layout.item_sticker_receive;
    private static final int SEND_VIDEO = R.layout.item_video_send;
    private static final int RECEIVE_VIDEO = R.layout.item_video_receive;
    private static final int SEND_LOCATION = R.layout.item_location_send;
    private static final int RECEIVE_LOCATION = R.layout.item_location_receive;
    private static final int RECEIVE_NOTIFICATION = R.layout.item_notification;
    private static final int RECEIVE_VOICE = R.layout.item_audio_receive;
    private static final int SEND_VOICE = R.layout.item_audio_send;
    private static final int RECEIVE_RED_PACKET = R.layout.item_red_packet_receive;
    private static final int SEND_RED_PACKET = R.layout.item_red_packet_send;
    private static final int UNDEFINE_MSG = R.layout.item_no_support_msg_type;
    private static final int RECALL_NOTIFICATION = R.layout.item_notification;
    //点击时间间隔
    private static final int MESSAGE_CLICK_INTERVAL = 1000;
    private static Long LINK_MESSAGE_CLICK_TIME = 0L;

    public ChatAdapter(Context context, List<Message> data, ChatPresenter presenter, RecyclerView rvMsg) {
        super(context, data);
        mContext = context;
        mData = data;
        mPresenter = presenter;
        mRvMsg = rvMsg;
    }

    @Override
    public void convert(LQRViewHolderForRecyclerView helper, Message item, int position) {
        helper.getView(R.id.loading_view).setVisibility(View.GONE);
        setTime(helper, item, position);
        setView(helper, item, position);
        if (getItemViewType(position) != UNDEFINE_MSG) {
            setAvatar(helper, item, position);
            setName(helper, item, position);
            setStatus(helper, item, position);
            setOnClick(helper, item, position);
        }
    }

    private void setView(LQRViewHolderForRecyclerView helper, Message item, int position) {
        //根据消息类型设置消息显示内容
        int msgType = item.getMsgType();
        switch (msgType) {
            case ChatConstant.CHAT_MSG_TYPE_TEXT: // 文本消息（表情包）
                TextMessageHelper.setTextMessageView(mContext, helper, item, position);
                break;
            case ChatConstant.CHAT_MSG_TYPE_MIXTURE: // 链接类型
                LinkMessageHelper.setLinkMessageView(mContext, helper, item, position);
                break;
            case ChatConstant.CHAT_MSG_TYPE_IMAGE: // 图片消息
                ImageMessageHelper.setImageMessageView(mContext, helper, item, position, mData);
                break;
            case ChatConstant.CHAT_MSG_TYPE_VIDEO: // 视频消息
                VideoMessageHelper.setVideoMessageView(mContext, helper, item, position);
                break;
            default:
                break;
        }
    }

    private void setOnClick(LQRViewHolderForRecyclerView helper, final Message item, final int position) {
        View errorView = helper.getView(R.id.llError);
        if (errorView != null) {
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 重新发送
                    //ToastHelper.showCenterToast("重新发送");
                    if (mItemClickListener != null) {
                        mItemClickListener.sendErrorClick(v, item, position);
                    }
                }
            });
        }

        // 点击头像跳转
        helper.getView(R.id.ivAvatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.avatarItemClick(v, item, position);
                }
            }
        });

        //点击聊天消息跳转(链接)
        if (item.getMsgType() == ChatConstant.CHAT_MSG_TYPE_MIXTURE) {
            helper.getView(R.id.ll_link_content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLinkMessageClick(item);
                }
            });

        }

        int itemViewType = getItemViewType(position);
        if (itemViewType == SEND_TEXT || itemViewType == RECEIVE_TEXT) {//文本
            TextMessageHelper.showTextMessageOperationBar(mContext, helper, item, helper.getAdapterPosition());
        } else if (itemViewType == SEND_IMAGE || itemViewType == RECEIVE_IMAGE) {//图片

        }
    }

    private void setStatus(LQRViewHolderForRecyclerView helper, Message item, int position) {
        int msgType = item.getMsgType();
        if (msgType == ChatConstant.CHAT_MSG_TYPE_TEXT) { // 文本消息
            TextMessageHelper.setTextStatus(helper, item, position);  //只需要设置自己发送的状态
        } else if (msgType == ChatConstant.CHAT_MSG_TYPE_IMAGE) { // 图片信息
            //TODO 图片信息
            ImageMessageHelper.setImageStatus(helper, item, position);
        } else if (msgType == ChatConstant.CHAT_MSG_TYPE_VIDEO) {
            //TODO 视频信息
            VideoMessageHelper.setVideoStatus(helper, item, position);
        } else if (msgType == ChatConstant.CHAT_MSG_TYPE_MIXTURE) {
            //TODO 链接信息
            LinkMessageHelper.setLinkStatus(helper, item, position);
        }
    }

    private void setAvatar(LQRViewHolderForRecyclerView helper, Message item, int position) {
        ImageView ivAvatar = helper.getView(R.id.ivAvatar);
        String avatar = "";
        if (item.getMessageCreatorType() == ChatConstant.CHAT_IDENTIFY_USER) {
            // 用户类型
            ChatUserInfo chatUserInfo = (ChatUserInfo) item.getMessageCreator();
            if (chatUserInfo != null) {
                avatar = chatUserInfo.getUserAvatar();
            }
            GlideUtil.loadFilletImage(mContext, avatar, R.drawable.icon_chat_placeholder, R.drawable.icon_chat_placeholder, ivAvatar);
        } else {
            ChatRobotInfo chatRobotInfo = (ChatRobotInfo) item.getMessageCreator();
            if (chatRobotInfo != null) {
                avatar = chatRobotInfo.getRobotAvatar();
            }
            if (!TextUtils.isEmpty(avatar)) {
                GlideUtil.loadFilletImage(mContext, avatar, R.mipmap.ic_launcher, R.mipmap.ic_launcher, ivAvatar);
            } else {
                GlideUtil.loadFilletImage(mContext, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, ivAvatar);
            }
            //LogUtils.info("输出avatar：" + avatar);
        }
    }

    private void setName(LQRViewHolderForRecyclerView helper, Message item, int position) {
        helper.setViewVisibility(R.id.tvName, View.GONE);
    }

    private void setTime(LQRViewHolderForRecyclerView helper, Message item, int position) {
        //数据库保存的是十位数的时间戳，java格式化需要的是13位数的时间戳，需要乘1000
        long msgTime = item.getCreateTime() * 1000;
        if (position < mData.size() - 1) {
            Message preMsg = mData.get(position + 1);
            long preMsgTime = preMsg.getCreateTime() * 1000;
            if (msgTime - preMsgTime > (5 * 60 * 1000)) {
                helper.setViewVisibility(R.id.tvTime, View.VISIBLE).setText(R.id.tvTime, TimeUtils.getMsgFormatTime(msgTime));
            } else {
                helper.setViewVisibility(R.id.tvTime, View.GONE);
            }
        } else {
            helper.setViewVisibility(R.id.tvTime, View.VISIBLE).setText(R.id.tvTime, TimeUtils.getMsgFormatTime(msgTime));
        }
    }

    //==================================== 各类型消息的点击回调 ===================================

    /**
     * 链接消息的点击回调
     */
    private void onLinkMessageClick(Message item) {
        if (mItemClickListener != null) {
            if (System.currentTimeMillis() - LINK_MESSAGE_CLICK_TIME > MESSAGE_CLICK_INTERVAL) {//快速点击拦截
                LinkMessage linkMessage = (LinkMessage) item.getMsgContent();
                HashMap<String, String> params = new HashMap<>();
                switch (linkMessage.getType()) {
                    case MessageConstant.TYPE_MESSAGE_LINK_H5:
                        // 发送事件，防止ChatActivity多次发送Copy到的文本
                        params.put(HomeConstant.TITLE, linkMessage.getTitle());
                        params.put(HomeNavigatorConstant.ROUTER_VALUE, linkMessage.getLinkUrl());
                        RouterNavigator.handleAppInternalNavigator(mContext, RouterNavigator.bannerRouterReflectMap.get(5), params, -1);
                        break;
                    case MessageConstant.TYPE_MESSAGE_LINK_NATIVE:
                        params.put(ShopDetailConstant.ITEM_ID, linkMessage.getId());
                        RouterNavigator.handleAppInternalNavigator(mContext, RouterNavigator.bannerRouterReflectMap.get(4), params, -1);
                        break;
                    case MessageConstant.TYPE_MESSAGE_LINK_SEARCH:
                        params.put(ShoppingSearchConstant.KEY_SEARCH_KEYWORD, linkMessage.getId());
                        RouterNavigator.handleAppInternalNavigator(mContext, HomeNavigatorConstant.NAV_GOOD_SEARCH, params, -1);
                        break;
                }
                EventBusManager.postEvent(ChatEventConstant.EVENT_BLOCK_COPY_FROM_WEB, true);
                LINK_MESSAGE_CLICK_TIME = System.currentTimeMillis();
            }
        }
    }


    // 以布局的id 来区分不同的条目类型，父类可以直接使用返回的布局ID
    @Override
    public int getItemViewType(int position) {
        Message msg = mData.get(position);
        // 发送还是接收
        boolean isSend = msg.getMessageCreatorType() == ChatConstant.CHAT_IDENTIFY_USER;
        int msgType = msg.getMsgType();
        switch (msgType) {
            case ChatConstant.CHAT_MSG_TYPE_TEXT:
                return isSend ? SEND_TEXT : RECEIVE_TEXT;
            case ChatConstant.CHAT_MSG_TYPE_IMAGE:
                return isSend ? SEND_IMAGE : RECEIVE_IMAGE;
            case ChatConstant.CHAT_MSG_TYPE_VIDEO:
                return isSend ? SEND_VIDEO : RECEIVE_VIDEO;
            case ChatConstant.CHAT_MSG_TYPE_MIXTURE:
                return isSend ? SEND_LINK : RECEIVE_LINK;
            case ChatConstant.CHAT_MSG_TYPE_AUDIO:
                break;
            default:
                break;
        }
        return UNDEFINE_MSG;
    }


    private ItemClickListener mItemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void sendErrorClick(View view, Message item, int position);

        void avatarItemClick(View view, Message item, int position);
    }

}
