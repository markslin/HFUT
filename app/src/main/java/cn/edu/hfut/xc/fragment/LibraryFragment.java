package cn.edu.hfut.xc.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hfut.xc.adapter.LibAdapter;
import cn.edu.hfut.xc.hfut.R;
import cn.edu.hfut.xc.struct.Book;
import cn.edu.hfut.xc.utilitis.LibraryUtil;
import cn.edu.hfut.xc.view.ColorProgressBar;

/**
 * Created by MarksLin on 2015/11/1 0001.
 */
public class LibraryFragment extends BaseFragment implements LibraryUtil.ThreadStatusListener, LibAdapter.CertifyCodeListener {
    private final int LOAD_DATA_OK = 1, LOAD_DATA_FAIL = 2;
    private ListView listView;
    private LibAdapter adapter;
    private Bitmap bitmap;
    private TextView bookName, borrowNum, bookAttach, bookBorrow, bookBorrowInfo;
    private Handler handler;
    private List<Book> books = new ArrayList<>();
    private Context context;
    private ColorProgressBar progressBar;

    public LibraryFragment getInstance(String... args) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("LIB_USERNAME", args[0]);
        bundle.putString("LIB_PASSWORD", args[1]);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, null);
        findViews(view);
        context = getActivity();
        progressBar.setVisibility(View.VISIBLE);
        setThemeColor(colorPrimary);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case LOAD_DATA_OK:
                        getData();
                        break;
                    case LOAD_DATA_FAIL:
                        LibraryUtil libraryUtil = new LibraryUtil(getArguments().getString("LIB_USERNAME"), getArguments().getString("LIB_PASSWORD"));
                        libraryUtil.setOnThreadStatusListener(LibraryFragment.this);
                        libraryUtil.start();
                        break;
                    default:
                        break;
                }
            }
        };
        LibraryUtil libraryUtil = new LibraryUtil(getArguments().getString("LIB_USERNAME"), getArguments().getString("LIB_PASSWORD"));
        libraryUtil.setOnThreadStatusListener(this);
        libraryUtil.start();
        return view;
    }

    private void getData() {
        adapter = new LibAdapter(books);
        adapter.setCodeBitmap(bitmap);
        adapter.setOnCertifyCodeListener(this);
        setThemeColor(colorPrimary);
        progressBar.setVisibility(View.INVISIBLE);
        listView.setAdapter(adapter);
        bookBorrowInfo.setText("当前借阅(" + books.size() + ")/最大借阅(15)");
    }

    private Drawable getDrawable(int color, Drawable drawable) {
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    @Override
    public void setThemeColor(int color) {
        color = Color.argb(0xff, Color.red(color), Color.green(color), Color.blue(color));
        progressBar.setIndeterminateDrawableColor(color);
        color = Color.argb(0x87, Color.red(color), Color.green(color), Color.blue(color));
        bookName.setBackground(getDrawable(color, bookName.getBackground()));
        borrowNum.setBackground(getDrawable(color, borrowNum.getBackground()));
        bookAttach.setBackground(getDrawable(color, bookAttach.getBackground()));
        bookBorrow.setBackground(getDrawable(color, bookBorrow.getBackground()));
        if (adapter != null)
            adapter.setThemeColor(color);
    }

    private void findViews(View view) {
        listView = (ListView) view.findViewById(R.id.fragment_library_list_view);
        bookName = (TextView) view.findViewById(R.id.book_name);
        borrowNum = (TextView) view.findViewById(R.id.book_borrow_num);
        bookAttach = (TextView) view.findViewById(R.id.book_attach);
        bookBorrow = (TextView) view.findViewById(R.id.book_borrow);
        bookBorrowInfo = (TextView) view.findViewById(R.id.book_borrow_info);
        progressBar = (ColorProgressBar) view.findViewById(R.id.fragment_library_color_progress_bar);
    }

    @Override
    public void threadStart() {

    }

    @Override
    public void threadEnd(Bitmap bitmap, List<Book> books) {
        this.bitmap = bitmap;
        this.books = books;
        handler.sendEmptyMessage(LOAD_DATA_OK);
    }

    @Override
    public void threadError() {
        handler.sendEmptyMessage(LOAD_DATA_FAIL);
    }

    @Override
    public void onGetCode(String code) {
        Toast.makeText(context, code, Toast.LENGTH_SHORT).show();
    }
}
