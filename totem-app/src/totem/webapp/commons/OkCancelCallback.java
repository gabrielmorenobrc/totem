package totem.webapp.commons;

import org.apache.wicket.ajax.AjaxRequestTarget;

import java.io.Serializable;


public interface OkCancelCallback extends Serializable {
    void onOk(AjaxRequestTarget ajaxRequestTarget);
    void onCancel(AjaxRequestTarget ajaxRequestTarget);
}
