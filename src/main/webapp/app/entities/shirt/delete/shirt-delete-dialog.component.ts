import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IShirt } from '../shirt.model';
import { ShirtService } from '../service/shirt.service';

@Component({
  templateUrl: './shirt-delete-dialog.component.html',
})
export class ShirtDeleteDialogComponent {
  shirt?: IShirt;

  constructor(protected shirtService: ShirtService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shirtService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
