package ch.epfl.sweng.project.data;

import android.content.Context;

import ch.epfl.sweng.project.chat.MessageAdapter;

public class ChatProvider {
    public static final String FIREBASE_PROVIDER = "Firebase";
    public static final String TEST_PROVIDER = "Tests";

    public static String mProvider = FIREBASE_PROVIDER;
    private final MessageAdapter mAdapter;
    private final Context mContext;


    public ChatProvider(Context context, MessageAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }

    public ChatHelper getTaskProvider() {
        switch (mProvider) {
            case FIREBASE_PROVIDER:
                return new FirebaseChatHelper(mContext, mAdapter);
            case TEST_PROVIDER:
                return new LocalChatHelper(mContext, mAdapter);
            default:
                throw new IllegalArgumentException("This provider does not exists !");
        }
    }

    public static void setProvider(String provider) {
        mProvider = provider;
    }
}
