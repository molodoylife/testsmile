package ua.hanasaka.tasksmilemolodykh.textmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.hanasaka.tasksmilemolodykh.OtherUserTwits;
import ua.hanasaka.tasksmilemolodykh.database.DB;


/**
 * class for work with text analyzing and converting user names in spannable string
 * <p>
 * Created by Oleksandr Molodykh on 24.03.2017.
 */

public class TextTwitManager {

    /**
     * changing matches to spannable string
     *
     * @param init init String
     * @param ctx  context
     * @return SpannableString
     */
    public static SpannableString changeNamesInText(String init, final Context ctx) {
        Pattern p = Pattern.compile("@(\\w+)");
        Matcher m = p.matcher(init);
        SpannableString spannableString = new SpannableString(init);
        while (m.find()) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                /**
                 * performing onClick for spanned text
                 *
                 * @param textView textview to work with
                 */
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
            if (isSameUser) {
                spannableString.setSpan(clickableSpan, m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new RelativeSizeSpan(1.3f), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    /**
     * checks if the user with same nick is in db
     *
     * @param ctx  context
     * @param nick nickname to analyzing
     * @return true if user with same name is in db
     */
    private static boolean isUserInDB(Context ctx, String nick) {
        DB db = DB.getInstance(ctx);
        db.open();
        long id = db.ifUserIsInDB(nick);
        Log.i("myLogs", "id="+id);
        return id > 0;
    }
}
