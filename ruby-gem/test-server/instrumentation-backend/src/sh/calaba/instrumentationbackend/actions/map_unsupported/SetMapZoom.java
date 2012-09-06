package sh.calaba.instrumentationbackend.actions.map_unsupported;

public class SetMapZoom extends UnsupportedMapAction {
  @Override
  public String key() {
    return "set_map_zoom";
  }
}