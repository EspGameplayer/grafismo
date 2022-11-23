import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGraphicElementPos } from '../graphic-element-pos.model';
import { GraphicElementPosService } from '../service/graphic-element-pos.service';

@Component({
  templateUrl: './graphic-element-pos-delete-dialog.component.html',
})
export class GraphicElementPosDeleteDialogComponent {
  graphicElementPos?: IGraphicElementPos;

  constructor(protected graphicElementPosService: GraphicElementPosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.graphicElementPosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
