# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# keep all public classes, otherwise they will be removed by tree-shaking since they may be unused within this library
-keep public class com.thumbtack.thumbprint.** {
  # also keep all public methods; this is necessary to keep file-level functions
  public <methods>;

  # also keep the companion object class; this appears to be necessary to reference companion object functions
  # statically (i.e. MyClass.foo() rather than MyClass.Companion.foo()), although it's not entirely clear why
  # See: https://jakewharton.com/r8-optimization-staticization/
  public static ** Companion;
}
