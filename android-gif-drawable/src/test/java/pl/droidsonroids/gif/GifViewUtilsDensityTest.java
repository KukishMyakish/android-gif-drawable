package pl.droidsonroids.gif;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static android.util.DisplayMetrics.DENSITY_DEFAULT;
import static android.util.DisplayMetrics.DENSITY_HIGH;
import static android.util.DisplayMetrics.DENSITY_LOW;
import static android.util.DisplayMetrics.DENSITY_MEDIUM;
import static android.util.DisplayMetrics.DENSITY_TV;
import static android.util.DisplayMetrics.DENSITY_XHIGH;
import static android.util.DisplayMetrics.DENSITY_XXHIGH;
import static android.util.DisplayMetrics.DENSITY_XXXHIGH;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static pl.droidsonroids.gif.GifViewUtils.getDensityScale;

@RunWith(MockitoJUnitRunner.class)
public class GifViewUtilsDensityTest {

	@Mock
	Resources resources;

	private Resources getMockedResources(final int resourceDensity, final int displayDensity) {
		doAnswer((Answer<TypedValue>) invocation -> {
      final TypedValue outValue = invocation.getArgument(1);
      outValue.density = resourceDensity;
      return outValue;
    }).when(resources).getValue(anyInt(), any(TypedValue.class), anyBoolean());

		final DisplayMetrics displayMetrics = new DisplayMetrics();
		displayMetrics.densityDpi = displayDensity;
		when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
		return resources;
	}

	@Test
	public void testHighResourceDensities() {
		assertThat(getDensityScale(getMockedResources(DENSITY_HIGH, DENSITY_HIGH), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(DENSITY_HIGH, DENSITY_LOW), 0)).isEqualTo(0.5f);
		assertThat(getDensityScale(getMockedResources(DENSITY_HIGH, DENSITY_MEDIUM), 0)).isEqualTo(2f / 3);
		assertThat(getDensityScale(getMockedResources(DENSITY_HIGH, DENSITY_XHIGH), 0)).isEqualTo(4f / 3);
		assertThat(getDensityScale(getMockedResources(DENSITY_HIGH, DENSITY_XXHIGH), 0)).isEqualTo(2);
		assertThat(getDensityScale(getMockedResources(DENSITY_HIGH, DENSITY_XXXHIGH), 0)).isEqualTo(8f / 3);
		assertThat(getDensityScale(getMockedResources(DENSITY_HIGH, DENSITY_TV), 0)).isEqualTo(213f / 240);
	}

	@Test
	public void testLowHighDensity() {
		assertThat(getDensityScale(getMockedResources(DENSITY_LOW, DENSITY_HIGH), 0)).isEqualTo(2);
	}

	@Test
	public void testNoneResourceDensities() {
		assertThat(getDensityScale(getMockedResources(TypedValue.DENSITY_NONE, DENSITY_LOW), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(TypedValue.DENSITY_NONE, DENSITY_MEDIUM), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(TypedValue.DENSITY_NONE, DENSITY_DEFAULT), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(TypedValue.DENSITY_NONE, DENSITY_TV), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(TypedValue.DENSITY_NONE, DENSITY_HIGH), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(TypedValue.DENSITY_NONE, DENSITY_XXHIGH), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(TypedValue.DENSITY_NONE, DENSITY_XXXHIGH), 0)).isEqualTo(1);
	}

	@Test
	public void testNoneDisplayDensities() {
		assertThat(getDensityScale(getMockedResources(DENSITY_LOW, Bitmap.DENSITY_NONE), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(DENSITY_MEDIUM, Bitmap.DENSITY_NONE), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(TypedValue.DENSITY_DEFAULT, Bitmap.DENSITY_NONE), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(DENSITY_HIGH, Bitmap.DENSITY_NONE), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(DENSITY_XXHIGH, Bitmap.DENSITY_NONE), 0)).isEqualTo(1);
	}

	@Test
	public void testInvalidDensities() {
		assertThat(getDensityScale(getMockedResources(DENSITY_HIGH, -1), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(-1, DENSITY_HIGH), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(-1, -1), 0)).isEqualTo(1);
		assertThat(getDensityScale(getMockedResources(DENSITY_HIGH, 0), 0)).isEqualTo(1);
	}
}