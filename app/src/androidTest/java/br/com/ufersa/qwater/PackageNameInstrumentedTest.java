package br.com.ufersa.qwater;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Teste de instrumentação que confere o pacote do aplicativo no dispositivo.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PackageNameInstrumentedTest {

    @Test
    public void packageNameMatchesTheApplicationId() {
        String packageName = InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName();

        assertEquals("br.com.ufersa.qwater", packageName);
    }
}
