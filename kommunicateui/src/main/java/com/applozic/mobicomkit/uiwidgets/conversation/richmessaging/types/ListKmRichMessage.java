package com.applozic.mobicomkit.uiwidgets.conversation.richmessaging.types;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.uiwidgets.AlCustomizationSettings;
import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.conversation.richmessaging.KmRichMessage;
import com.applozic.mobicomkit.uiwidgets.conversation.richmessaging.adapters.KmListRMAdapter;
import com.applozic.mobicomkit.uiwidgets.conversation.richmessaging.adapters.KmRichMessageAdapterFactory;
import com.applozic.mobicomkit.uiwidgets.conversation.richmessaging.callbacks.KmRichMessageListener;
import com.applozic.mobicomkit.uiwidgets.conversation.richmessaging.models.KmRichMessageModel;
import com.applozic.mobicommons.json.GsonUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class ListKmRichMessage extends KmRichMessage {

    public static final int MAX_ACTIONS_LIMIT = 3;

    public ListKmRichMessage(Context context, LinearLayout containerView, Message message, KmRichMessageListener listener, AlCustomizationSettings alCustomizationSettings) {
        super(context, containerView, message, listener, alCustomizationSettings);
    }

    @Override
    public void createRichMessage() {
        super.createRichMessage();

        if (model != null) {
            if (model.getPayload() != null) {
                TextView headerText = listItemLayout.findViewById(R.id.headerText);
                ImageView headerImage = listItemLayout.findViewById(R.id.headerImage);
                KmRichMessageModel.KmPayloadModel payload = (KmRichMessageModel.KmPayloadModel) GsonUtils.getObjectFromJson(model.getPayload(), KmRichMessageModel.KmPayloadModel.class);
                if (payload != null) {
                    RecyclerView listRecycler = listItemLayout.findViewById(R.id.alListItemRecycler);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    listRecycler.setLayoutManager(layoutManager);
                    KmListRMAdapter adapter = (KmListRMAdapter) KmRichMessageAdapterFactory.getInstance().getListRMAdapter(context, message, payload.getElements(), payload.getReplyMetadata(), listener, alCustomizationSettings);
                    listRecycler.setAdapter(adapter);

                    if (!TextUtils.isEmpty(payload.getHeaderText())) {
                        headerText.setVisibility(View.VISIBLE);
                        headerText.setText(getHtmlText(payload.getHeaderText()));
                    } else {
                        headerText.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(payload.getHeaderImgSrc())) {
                        headerImage.setVisibility(View.VISIBLE);
                        Glide.with(context).load(payload.getHeaderImgSrc()).into(headerImage);
                    } else {
                        headerImage.setVisibility(View.GONE);
                    }

                    if (payload.getButtons() != null) {
                        final List<KmRichMessageModel.KmButtonModel> action = payload.getButtons();

                        if (action.get(0) != null) {
                            setActionTextView((TextView) listItemLayout.findViewById(R.id.actionButton1), null, action.get(0), payload, model);
                        }

                        if (action.size() > 1 && action.get(1) != null) {
                            setActionTextView((TextView) listItemLayout.findViewById(R.id.actionButton2), listItemLayout.findViewById(R.id.actionDivider2), action.get(1), payload, model);
                        }

                        if (action.size() > 2 && action.get(2) != null) {
                            setActionTextView((TextView) listItemLayout.findViewById(R.id.actionButton3), listItemLayout.findViewById(R.id.actionDivider3), action.get(2), payload, model);
                        }
                    }
                }
            }
        }
    }

    private void setActionTextView(TextView actionTextView, View actionDivider, KmRichMessageModel.KmButtonModel buttonModel, KmRichMessageModel.KmPayloadModel payload, KmRichMessageModel model) {
        actionTextView.setVisibility(View.VISIBLE);
        actionTextView.setText(buttonModel.getName());
        actionTextView.setTextColor(themeHelper.getRichMessageThemeColor());
        setActionListener(actionTextView, model, buttonModel, payload);

        if (actionDivider != null) {
            actionDivider.setVisibility(View.VISIBLE);
        }
    }
}
