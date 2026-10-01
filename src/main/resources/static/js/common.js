/**
 * 全站通用自訂 Alert 彈窗
 */
function showCustomAlert(message, callback, title) {
  if ($("#customAlertModal").length === 0) {
    const modalHtml = `
      <div class="modal fade" id="customAlertModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title fw-bold" id="customAlertTitle">お知らせ</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body" id="customAlertMessage" style="white-space: pre-wrap;"></div>
            <div class="modal-footer">
              <button type="button" class="btn main-btn px-4" data-bs-dismiss="modal">OK</button>
            </div>
          </div>
        </div>
      </div>
    `;
    $("body").append(modalHtml);
  }

  $("#customAlertTitle").text(title || "お知らせ");
  $("#customAlertMessage").text(message);

  const modalEl = document.getElementById("customAlertModal");
  const alertModal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);

  $(modalEl).off("hidden.bs.modal").on("hidden.bs.modal", function () {
    if (typeof callback === "function") callback();
  });

  alertModal.show();
}

/**
 * 全站通用自訂 Confirm 確認對話框
 * @param {string} message - 詢問訊息
 * @param {function} onConfirm - 按下「確定」後執行的 callback
 * @param {string} [title="確認"] - 標題
 */
function showCustomConfirm(message, onConfirm, title) {
  if ($("#customConfirmModal").length === 0) {
    const modalHtml = `
      <div class="modal fade" id="customConfirmModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title fw-bold" id="customConfirmTitle">確認</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body" id="customConfirmMessage" style="white-space: pre-wrap;"></div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">キャンセル</button>
              <button type="button" id="customConfirmOkBtn" class="btn btn-danger px-4">削除する</button>
            </div>
          </div>
        </div>
      </div>
    `;
    $("body").append(modalHtml);
  }

  $("#customConfirmTitle").text(title || "確認");
  $("#customConfirmMessage").text(message);

  const modalEl = document.getElementById("customConfirmModal");
  const confirmModal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);

  // 綁定「確定/刪除」按鈕點擊事件
  $("#customConfirmOkBtn").off("click").on("click", function () {
    confirmModal.hide();
    if (typeof onConfirm === "function") {
      onConfirm();
    }
  });

  confirmModal.show();
}