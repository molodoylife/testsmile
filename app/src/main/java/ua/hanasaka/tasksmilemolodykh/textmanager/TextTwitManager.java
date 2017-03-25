package ua.hanasaka.tasksmilemolodykh.textmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.hanasaka.tasksmilemolodykh.OtherUserTwits;
import ua.hanasaka.tasksmilemolodykh.database.DB;


/**
 * Created by Oleksandr Molodykh on 24.03.2017.
 */

public class TextTwitManager {
    final static String TAG = "myLogs";

    public static SpannableString changeNamesInText(String init, final Context ctx){
        Pattern p = Pattern.compile("@(\\w+)");
        Matcher m = p.matcher(init);
        SpannableString ss = new SpannableString(init);
        while(m.find()){
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    TextView tv = (TextView) textView;
                    Spanned spanned = (Spanned) tv.getText();
                    int start = spanned.getSpanStart(this);
                    int end = spanned.getSpanEnd(this);
                    String nick = spanned.subSequence(start, end).toString();
                    Intent intent = new Intent(ctx, OtherUserTwits.class);
                    intent.putExtra("nick", nick);
                    ctx.startActivity(intent);
                }
                @Override
                public void updateDrawState(TextPaint tp) {
                    super.updateDrawState(tp);
                    tp.setColor(Color.parseColor("#54ABEE"));
                    tp.setUnderlineText(false);
                }
            };
            boolean isSameUser = isUserInDB(ctx, m.group());
            if(isSameUser) {
                ss.setSpan(clickableSpan, m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new RelativeSizeSpan(1.3f), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ss;
    }

    private static boolean isUserInDB(Context ctx, String nick){
        DB db = DB.getInstance(ctx);
        db.open();
        long id = db.ifUserIsInDB(nick);
        if(id>0)
            return true;
        else
            return false;
    }
}
