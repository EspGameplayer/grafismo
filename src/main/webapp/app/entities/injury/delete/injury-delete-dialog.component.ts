import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInjury } from '../injury.model';
import { InjuryService } from '../service/injury.service';

@Component({
  templateUrl: './injury-delete-dialog.component.html',
})
export class InjuryDeleteDialogComponent {
  injury?: IInjury;

  constructor(protected injuryService: InjuryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.injuryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
