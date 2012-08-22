package com.intellij.jps.flex.model.bc.impl;

import com.intellij.jps.flex.model.bc.JpsAirPackageEntry;
import com.intellij.jps.flex.model.bc.JpsIosPackagingOptions;
import com.intellij.util.xmlb.annotations.AbstractCollection;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementCreator;
import org.jetbrains.jps.model.impl.JpsElementChildRoleBase;
import org.jetbrains.jps.model.impl.JpsElementCollectionImpl;

import java.util.ArrayList;
import java.util.List;

class JpsIosPackagingOptionsImpl extends JpsAirPackagingOptionsBase<JpsIosPackagingOptionsImpl> implements JpsIosPackagingOptions {

  static final JpsIosPackagingOptionsRole ROLE = new JpsIosPackagingOptionsRole();

  private JpsIosPackagingOptionsImpl() {
  }

  private JpsIosPackagingOptionsImpl(final JpsAirPackagingOptionsBase<JpsIosPackagingOptionsImpl> original) {
    super(original);
  }

  @NotNull
  public JpsIosPackagingOptionsImpl createCopy() {
    return new JpsIosPackagingOptionsImpl(this);
  }

  State getState() {
    final State state = new State();
    state.ENABLED = isEnabled();
    state.USE_GENERATED_DESCRIPTOR = isUseGeneratedDescriptor();
    state.CUSTOM_DESCRIPTOR_PATH = getCustomDescriptorPath();
    state.PACKAGE_FILE_NAME = getPackageFileName();

    for (JpsAirPackageEntry entry : getFilesToPackage()) {
      state.FILES_TO_PACKAGE.add(((JpsAirPackageEntryImpl)entry).getState());
    }

    state.SIGNING_OPTIONS = ((JpsAirSigningOptionsImpl)getSigningOptions()).getState();

    return state;
  }

  void loadState(@NotNull State state) {
    setEnabled(state.ENABLED);
    setUseGeneratedDescriptor(state.USE_GENERATED_DESCRIPTOR);
    setCustomDescriptorPath(state.CUSTOM_DESCRIPTOR_PATH);
    setPackageFileName(state.PACKAGE_FILE_NAME);

    final JpsElementCollectionImpl<JpsAirPackageEntry> packageEntries = myContainer.getChild(JpsAirPackageEntryImpl.COLLECTION_ROLE);
    assert packageEntries.getElements().isEmpty();
    for (JpsAirPackageEntryImpl.State f : state.FILES_TO_PACKAGE) {
      final JpsAirPackageEntry entry = ((JpsElementCreator<JpsAirPackageEntry>)JpsAirPackageEntryImpl.ROLE).create();
      ((JpsAirPackageEntryImpl)entry).loadState(f);
      packageEntries.addChild(entry);
    }

    ((JpsAirSigningOptionsImpl)getSigningOptions()).loadState(state.SIGNING_OPTIONS);
  }

  private static class JpsIosPackagingOptionsRole extends JpsElementChildRoleBase<JpsIosPackagingOptions>
    implements JpsElementCreator<JpsIosPackagingOptions> {

    public JpsIosPackagingOptionsRole() {
      super("ios packaging options");
    }

    @NotNull
    public JpsIosPackagingOptions create() {
      return new JpsIosPackagingOptionsImpl();
    }
  }

  @Tag("packaging-ios")
  public static class State {
    @Attribute("enabled")
    public boolean ENABLED = false;
    @Attribute("use-generated-descriptor")
    public boolean USE_GENERATED_DESCRIPTOR = true;
    @Attribute("custom-descriptor-path")
    public String CUSTOM_DESCRIPTOR_PATH = "";
    @Attribute("package-file-name")
    public String PACKAGE_FILE_NAME = "";
    @Tag("files-to-package")
    @AbstractCollection(surroundWithTag = false)
    public List<JpsAirPackageEntryImpl.State> FILES_TO_PACKAGE = new ArrayList<JpsAirPackageEntryImpl.State>();
    @Property(surroundWithTag = false)
    public JpsAirSigningOptionsImpl.State SIGNING_OPTIONS = new JpsAirSigningOptionsImpl.State();
  }
}
