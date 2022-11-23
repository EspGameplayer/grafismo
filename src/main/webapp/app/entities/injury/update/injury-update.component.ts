import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInjury, Injury } from '../injury.model';
import { InjuryService } from '../service/injury.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';

@Component({
  selector: 'jhi-injury-update',
  templateUrl: './injury-update.component.html',
})
export class InjuryUpdateComponent implements OnInit {
  isSaving = false;

  playersSharedCollection: IPlayer[] = [];

  editForm = this.fb.group({
    id: [],
    moment: [],
    estReturnDate: [],
    reason: [],
    player: [null, Validators.required],
  });

  constructor(
    protected injuryService: InjuryService,
    protected playerService: PlayerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ injury }) => {
      if (injury.id === undefined) {
        const today = dayjs().startOf('day');
        injury.moment = today;
      }

      this.updateForm(injury);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const injury = this.createFromForm();
    if (injury.id !== undefined) {
      this.subscribeToSaveResponse(this.injuryService.update(injury));
    } else {
      this.subscribeToSaveResponse(this.injuryService.create(injury));
    }
  }

  trackPlayerById(_index: number, item: IPlayer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInjury>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(injury: IInjury): void {
    this.editForm.patchValue({
      id: injury.id,
      moment: injury.moment ? injury.moment.format(DATE_TIME_FORMAT) : null,
      estReturnDate: injury.estReturnDate,
      reason: injury.reason,
      player: injury.player,
    });

    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing(this.playersSharedCollection, injury.player);
  }

  protected loadRelationshipsOptions(): void {
    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(map((players: IPlayer[]) => this.playerService.addPlayerToCollectionIfMissing(players, this.editForm.get('player')!.value)))
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));
  }

  protected createFromForm(): IInjury {
    return {
      ...new Injury(),
      id: this.editForm.get(['id'])!.value,
      moment: this.editForm.get(['moment'])!.value ? dayjs(this.editForm.get(['moment'])!.value, DATE_TIME_FORMAT) : undefined,
      estReturnDate: this.editForm.get(['estReturnDate'])!.value,
      reason: this.editForm.get(['reason'])!.value,
      player: this.editForm.get(['player'])!.value,
    };
  }
}
