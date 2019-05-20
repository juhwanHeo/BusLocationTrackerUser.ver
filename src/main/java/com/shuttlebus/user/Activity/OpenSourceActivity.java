package com.shuttlebus.user.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shuttlebus.user.R;

public class OpenSourceActivity extends AppCompatActivity {

    private TextView license;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opensource_license);
        findViews();

        license.setText("Copyright [yyyy] [저작권자]\n" +
                "                Licensed under the Apache License, Version 2.0 (the 'License');\n" +
                "        you may not use this file except in compliance with the License.\n" +
                "                You may obtain a copy of the License at\n" +
                "\n" +
                "        http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "        Unless required by applicable law or agreed to in writing, software\n" +
                "        distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "                WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "                See the License for the specific language governing permissions and\n" +
                "        limitations under the License.\"");
    }

    private void findViews(){
        license = findViewById(R.id.license_tv);
    }
}