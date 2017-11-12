package cn.edu.hfut.xc.adapter;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hfut.xc.hfut.R;
import cn.edu.hfut.xc.struct.Book;
import cn.edu.hfut.xc.view.RippleView;

/**
 * Created by MarksLin on 2015/11/1 0001.
 */
public class LibAdapter extends BaseAdapter {
    List<Book> books;
    private List<TextView> textViews = new ArrayList<>();
    private List<LinearLayout> layouts = new ArrayList<>();
    private List<RippleView> rippleViews = new ArrayList<>();
    private int color = 0x87ff6600;
    private Bitmap bitmap;
    private CertifyCodeListener listener;

    public LibAdapter(List<Book> books) {
        this.books = books;
    }

    public void setOnCertifyCodeListener(CertifyCodeListener listener) {
        this.listener = listener;
    }

    public void setThemeColor(int color) {
        this.color = Color.argb(0x87, Color.red(color), Color.green(color), Color.blue(color));
        for (TextView textView : textViews) {
            if (textView.getText().equals("+1"))
                textView.setBackground(getDrawable(Color.argb(0xff, Color.red(color), Color.green(color), Color.blue(color)), textView.getBackground()));
            else
                textView.setBackground(getDrawable(this.color, textView.getBackground()));
        }
        for (LinearLayout layout : layouts) {
            layout.setBackground(getDrawable(this.color, layout.getBackground()));
        }
        for (RippleView rippleView : rippleViews) {
            rippleView.setBackground(getDrawable(this.color, rippleView.getBackground()));
        }
    }

    private Drawable getDrawable(int color, Drawable drawable) {
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public void setCodeBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        convertView = View.inflate(parent.getContext(), R.layout.library_list_item, null);
        LinearLayout title = (LinearLayout) convertView.findViewById(R.id.book_title);
        TextView name = (TextView) convertView.findViewById(R.id.book_name);
        TextView date = (TextView) convertView.findViewById(R.id.book_from_end_date);
        TextView num = (TextView) convertView.findViewById(R.id.book_borrow_num);
        TextView attach = (TextView) convertView.findViewById(R.id.book_attach);
        TextView borrow = (TextView) convertView.findViewById(R.id.book_borrow);
        RippleView rippleView = (RippleView) convertView.findViewById(R.id.book_ripple_view);
        Book book = books.get(position);
        name.setText(book.getName());
        date.setText(book.getDate());
        num.setText(book.getNum());
        attach.setText(book.getAttach());

        title.setBackground(getDrawable(color, title.getBackground()));
        num.setBackground(getDrawable(color, num.getBackground()));
        attach.setBackground(getDrawable(color, attach.getBackground()));
        borrow.setBackground(getDrawable(Color.argb(0xff, Color.red(color), Color.green(color), Color.blue(color)), borrow.getBackground()));
        rippleView.setBackground(getDrawable(color, rippleView.getBackground()));
        textViews.add(num);
        textViews.add(attach);
        textViews.add(borrow);
        layouts.add(title);
        rippleViews.add(rippleView);
        borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = View.inflate(parent.getContext(), R.layout.identify_code_dialog, null);
                final TextView negativeButton = (TextView) view.findViewById(R.id.negative_text_view);
                final TextView positiveButton = (TextView) view.findViewById(R.id.positive_text_view);
                final TextView tip = (TextView) view.findViewById(R.id.identify_code_tip);
                final View line = view.findViewById(R.id.identify_code_line);
                final ImageView codeImg = (ImageView) view.findViewById(R.id.identify_code_image_view);
                final EditText identifyCode = (EditText) view.findViewById(R.id.identify_code_edit_text);

                final int COLOR = Color.argb(0xff, Color.red(color), Color.green(color), Color.blue(color));
                tip.setTextColor(COLOR);
                negativeButton.setBackgroundColor(COLOR);
                positiveButton.setBackgroundColor(COLOR);
                line.setBackgroundColor(COLOR);
                identifyCode.setBackground(getDrawable(COLOR, identifyCode.getBackground()));
                identifyCode.setTextColor(COLOR);
                identifyCode.setHintTextColor(color);

                codeImg.setImageBitmap(bitmap);

                Toast.makeText(parent.getContext(), "下个版本努力开发中...", Toast.LENGTH_SHORT).show();
                /*final AlertDialog dialog = new AlertDialog.Builder(parent.getContext()).create();
                dialog.setView(View.inflate(parent.getContext(), R.layout.identify_code_dialog,null));
                dialog.show();
                dialog.getWindow().setContentView(view);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(parent.getContext(), "negativeButton" + identifyCode.getText(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(parent.getContext(), "positiveButton" + identifyCode.getText(), Toast.LENGTH_SHORT).show();
                        if (listener!=null)
                            listener.onGetCode(identifyCode.getText().toString());
                        dialog.dismiss();
                    }
                });*/
            }
        });
        return convertView;
    }

    public interface CertifyCodeListener {
        void onGetCode(String code);
    }
}
