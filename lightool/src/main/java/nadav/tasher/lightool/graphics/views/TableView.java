package nadav.tasher.lightool.graphics.views;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nadav.tasher.lightool.R;

public class TableView extends LinearLayout {
    static final int MODE_RW = 0;
    static final int MODE_RO = 1;
    static final int MODE_VO = 2;
    JSONArray currentData;
    String[] titles;
    int mode, size, textcolor;
    OnChanged onChanged;
    int removeButtonSize;
    boolean removeButton = false;

    public TableView(Context context) {
        super(context);
    }

    public TableView(Context context, int textcolor, int size, int mode, JSONArray currentData, String[] titles) {
        super(context);
        this.currentData = currentData;
        this.mode = mode;
        this.size = size;
        this.textcolor = textcolor;
        this.titles = titles;
        init();
    }

    public void setOnChanged(OnChanged onChanged) {
        this.onChanged = onChanged;
    }

    public void setShowRemoveButton(boolean isShown, int buttonSize) {
        removeButton = isShown;
        removeButtonSize = buttonSize;
        init();
    }

    private void init() {
        removeAllViews();
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        TableViewRow title = new TableViewRow(getContext(), textcolor, size, MODE_VO, titles, titles);
        addView(title);
        ScrollView rowScroll = new ScrollView(getContext());
        final LinearLayout rows = new LinearLayout(getContext());
        rows.setOrientation(LinearLayout.VERTICAL);
        rows.setGravity(Gravity.CENTER);
        rowScroll.addView(rows);
        addView(rowScroll);
        addListElements(rows);
        ImageButton addNew = new ImageButton(getContext());
        addNew.setImageDrawable(getContext().getDrawable(R.drawable.ic_add));
        addNew.setBackground(null);
        addNew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentData.put(new JSONObject());
                addListElements(rows);
                if (onChanged != null) onChanged.onChanged(currentData);
            }
        });
        if (mode == MODE_RW)
            addView(addNew);
    }

    private void addListElements(final LinearLayout list) {
        list.removeAllViews();
        for (int t = 0; t < currentData.length(); t++) {
            String[] current = new String[titles.length];
            try {
                JSONObject obj = currentData.getJSONObject(t);
                for (int a = 0; a < titles.length; a++) {
                    if (obj.has(titles[a].toLowerCase())) {
                        current[a] = obj.getString(titles[a].toLowerCase());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final int finalT = t;
            final TableViewRow row = new TableViewRow(getContext(), textcolor, size, mode, current, titles);
            if (removeButton) {
                row.showRemoveButton(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentData.remove(finalT);
                        addListElements(list);
                        if (onChanged != null) onChanged.onChanged(currentData);
                    }
                }, removeButtonSize);
            }
            list.addView(row);
            row.setOnChanged(new TableViewRow.OnChanged() {
                @Override
                public void onChanged(String name, String value) {
                    try {
                        JSONObject buildNew = new JSONObject();
                        for (int b = 0; b < titles.length; b++) {
                            buildNew.put(titles[b].toLowerCase(), row.getCurrentValues()[b]);
                        }
                        currentData.put(finalT, buildNew);
                        if (onChanged != null) onChanged.onChanged(currentData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    interface OnChanged {
        void onChanged(JSONArray nowData);
    }

    static class TableViewRow extends LinearLayout {
        int mode;
        int size;
        int textcolor;
        String[] currentValues;
        String[] hints;
        OnChanged onChanged;
        ImageButton remove;

        public TableViewRow(Context context) {
            super(context);
        }

        public TableViewRow(Context context, int textcolor, int size, int mode, String[] values, String[] hints) {
            super(context);
            this.mode = mode;
            currentValues = values;
            this.hints = hints;
            this.size = size;
            this.textcolor = textcolor;
            init();
        }

        public void setOnChanged(OnChanged onChanged) {
            this.onChanged = onChanged;
        }

        public String[] getCurrentValues() {
            return currentValues;
        }

        public void showRemoveButton(View.OnClickListener todo, int s) {
            remove.setLayoutParams(new LayoutParams(s, ViewGroup.LayoutParams.MATCH_PARENT));
            int c = 1;
            getChildAt(c).setLayoutParams(new LayoutParams(getChildAt(c).getLayoutParams().width - remove.getLayoutParams().width, getChildAt(c).getLayoutParams().height));
            remove.setVisibility(View.VISIBLE);
            remove.setOnClickListener(todo);
        }

        private void init() {
            setOrientation(HORIZONTAL);
            setGravity(Gravity.CENTER);
            remove = new ImageButton(getContext());
            remove.setImageDrawable(getContext().getDrawable(R.drawable.ic_delete));
            remove.setBackground(null);
            addView(remove);
            remove.setVisibility(View.GONE);
            for (int i = 0; i < currentValues.length; i++) {
                final TextView ctv;
                if (mode == TableView.MODE_VO) {
                    ctv = new TextView(getContext());
                } else if (mode == TableView.MODE_RO) {
                    ctv = new EditText(getContext());
                    ctv.setInputType(InputType.TYPE_NULL);
                    ctv.setTextIsSelectable(true);
                } else {
                    ctv = new EditText(getContext());
                }
                ctv.setTextColor(textcolor);
                if (hints.length == currentValues.length) {
                    ctv.setHint(hints[i]);
                }
                ctv.setTextSize(20);
                ctv.setText(currentValues[i]);
                final int finalI = i;
                ctv.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(final CharSequence s, int start, int before, int count) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (s.toString().equals(ctv.getText().toString())) {
                                    currentValues[finalI] = s.toString();
                                    if (onChanged != null && hints.length == currentValues.length) {
                                        onChanged.onChanged(hints[finalI], currentValues[finalI]);
                                    }
                                }
                            }
                        }, 100);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                ctv.setLayoutParams(new LayoutParams(size, ViewGroup.LayoutParams.WRAP_CONTENT));
                ctv.setGravity(Gravity.CENTER);
                addView(ctv);
            }
        }

        interface OnChanged {
            void onChanged(String name, String value);
        }
    }
}
