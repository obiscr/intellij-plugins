package com.intellij.jps.flex.model.bc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.JpsCompositeElement;
import org.jetbrains.jps.model.JpsNamedElement;
import org.jetbrains.jps.model.JpsReferenceableElement;
import org.jetbrains.jps.model.library.sdk.JpsSdk;
import org.jetbrains.jps.model.module.JpsModule;

import javax.swing.*;
import java.util.Collection;

public interface JpsFlexBuildConfiguration
  extends JpsNamedElement, JpsCompositeElement, JpsReferenceableElement<JpsFlexBuildConfiguration> {

  String UNNAMED = "Unnamed";

  JpsModule getModule();

  @NotNull
  String getName();

  @NotNull
  JpsTargetPlatform getTargetPlatform();

  boolean isPureAs();

  @NotNull
  JpsOutputType getOutputType();

  @NotNull
  String getOptimizeFor();

  @NotNull
  String getMainClass();

  /**
   * Returns output file name as set in UI. Note that actual output file name may be different if additional compiler config file is used: see {@link #getActualOutputFilePath()}
   */
  @NotNull
  String getOutputFileName();

  /**
   * Returns output folder path as set in UI. Note that actual output folder path may be different if additional compiler config file is used: see {@link #getActualOutputFilePath()}
   */
  @NotNull
  String getOutputFolder();

  /**
   * Returns output file path as set in additional compiler config file if it exists
   * and this is not a temporary build configuration for SWF compilation (i.e. if additional compiler config file is not merged with the generated one).
   * Otherwise returns <code>{@link #getOutputFolder()} + "/" + {@link #getOutputFileName()}</code>
   */
  //String getActualOutputFilePath();

  boolean isUseHtmlWrapper();

  @NotNull
  String getWrapperTemplatePath();

  @NotNull
  Collection<RLMInfo> getRLMs();

  @NotNull
  Collection<String> getCssFilesToCompile();

  boolean isSkipCompile();

  @NotNull
  JpsFlexDependencies getDependencies();

  @NotNull
  JpsFlexCompilerOptions getCompilerOptions();

  @NotNull
  JpsAirDesktopPackagingOptions getAirDesktopPackagingOptions();

  @NotNull
  JpsAndroidPackagingOptions getAndroidPackagingOptions();

  @NotNull
  JpsIosPackagingOptions getIosPackagingOptions();

  Icon getIcon();

  JpsBuildConfigurationNature getNature();

  @Nullable
  JpsSdk<?> getSdk();

  /**
   * Returns <code>true</code> if this is a temporary BC, i.e.  used for compilation of one of the following: <ul>
   * <li>FlexUnit runner</li>
   * <li>app with main class that is overridden in run configuration</li>
   * <li>css file</li>
   * <li>RLM that is configured at main app BC, i.e. not as a separate BC</li>
   * </ul>
   * <code>true</code> means that main class and output folder/file name from BC must win over overridden values from additional config file (in ordinary case additional config file wins).
   * I.e. <code>true</code> means that additional config file must not be used literally, but instead must be merged with the autogenerated one.
   */
  boolean isTempBCForCompilation();

  //boolean isEqual(FlexIdeBuildConfiguration other);

  String getShortText();

  String getDescription();

  String getStatisticsEntry();

  class RLMInfo {
    public final @NotNull String MAIN_CLASS;
    /**
     * path to output swf file relative to output folder, e.g. "com/foo/Module1.swf"
     */
    public final @NotNull String OUTPUT_FILE;
    public final boolean OPTIMIZE;

    public RLMInfo(final @NotNull String mainClass, final @NotNull String outputFileName, final boolean optimize) {
      this.MAIN_CLASS = mainClass;
      this.OUTPUT_FILE = outputFileName;
      this.OPTIMIZE = optimize;
    }

    public boolean equals(final Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final RLMInfo info = (RLMInfo)o;

      if (OPTIMIZE != info.OPTIMIZE) return false;
      if (!MAIN_CLASS.equals(info.MAIN_CLASS)) return false;
      if (!OUTPUT_FILE.equals(info.OUTPUT_FILE)) return false;

      return true;
    }

    public int hashCode() {
      int result = MAIN_CLASS.hashCode();
      result = 31 * result + OUTPUT_FILE.hashCode();
      result = 31 * result + (OPTIMIZE ? 1 : 0);
      return result;
    }
  }
}
