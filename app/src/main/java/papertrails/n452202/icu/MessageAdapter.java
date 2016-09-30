package papertrails.n452202.icu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public  class MessageAdapter extends ArrayAdapter<LocMessage> {

        private Context context;
        private List<LocMessage> items;
        private MessageVH messageVh;

        public MessageAdapter(Context context, List<LocMessage> objects) {
            super(context, -1, objects);
            this.items = objects;
            this.context = context;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_msg, null);
                messageVh = new MessageVH(convertView);
                convertView.setTag(messageVh);
            }else{

                messageVh = (MessageVH) convertView.getTag();
            }

            LocMessage message =  this.items.get(position);
            messageVh.updataView(message);

            return convertView;
        }


        public   class MessageVH{

            public TextView nameTextView;
            public  TextView locTextView;
            public  TextView timeTextView;

            public  MessageVH(View view){

                nameTextView =  ((TextView) view.findViewById(R.id.textView));
                locTextView =  ((TextView) view.findViewById(R.id.textView2));
                timeTextView =  ((TextView) view.findViewById(R.id.textView3));

            }

            public  void  updataView(LocMessage locMessage){

                nameTextView.setText(locMessage.getName());
                locTextView.setText(""+locMessage.getLat() + locMessage.getLng());
                timeTextView.setText(locMessage.getTime());

            }

        }

    }