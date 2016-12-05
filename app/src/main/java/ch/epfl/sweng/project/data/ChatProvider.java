package ch.epfl.sweng.project.data;

import android.content.Context;

import ch.epfl.sweng.project.Utils;
import ch.epfl.sweng.project.chat.MessageAdapter;

public class ChatProvider {

    public static String mProvider = Utils.FIREBASE_PROVIDER;
    private final MessageAdapter mAdapter;
    private final Context mContext;


    public ChatProvider(Context context, MessageAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }

    public ChatHelper getChatProvider() {
        switch (mProvider) {
            case Utils.FIREBASE_PROVIDER:
                return new FirebaseChatHelper(mContext, mAdapter);
            case Utils.TEST_PROVIDER:
                return new LocalChatHelper(mContext, mAdapter);
            default:
                throw new IllegalArgumentException("This provider does not exists !");
        }
    }

    public static void setProvider(String provider) {
        mProvider = provider;
    }
}
