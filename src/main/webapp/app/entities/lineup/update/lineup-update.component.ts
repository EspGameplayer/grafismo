import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILineup, Lineup } from '../lineup.model';
import { LineupService } from '../service/lineup.service';
import { ICallup } from 'app/entities/callup/callup.model';
import { CallupService } from 'app/entities/callup/service/callup.service';
import { IFormation } from 'app/entities/formation/formation.model';
import { FormationService } from 'app/entities/formation/service/formation.service';
import { IMatchPlayer } from 'app/entities/match-player/match-player.model';
import { MatchPlayerService } from 'app/entities/match-player/service/match-player.service';

@Component({
  selector: 'jhi-lineup-update',
  templateUrl: './lineup-update.component.html',
})
export class LineupUpdateComponent implements OnInit {
  isSaving = false;

  callupsCollection: ICallup[] = [];
  formationsSharedCollection: IFormation[] = [];
  matchPlayersSharedCollection: IMatchPlayer[] = [];

  editForm = this.fb.group({
    id: [],
    callup: [null, Validators.required],
    formation: [],
    players: [],
  });

  constructor(
    protected lineupService: LineupService,
    protected callupService: CallupService,
    protected formationService: FormationService,
    protected matchPlayerService: MatchPlayerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lineup }) => {
      this.updateForm(lineup);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lineup = this.createFromForm();
    if (lineup.id !== undefined) {
      this.subscribeToSaveResponse(this.lineupService.update(lineup));
    } else {
      this.subscribeToSaveResponse(this.lineupService.create(lineup));
    }
  }

  trackCallupById(_index: number, item: ICallup): number {
    return item.id!;
  }

  trackFormationById(_index: number, item: IFormation): number {
    return item.id!;
  }

  trackMatchPlayerById(_index: number, item: IMatchPlayer): number {
    return item.id!;
  }

  getSelectedMatchPlayer(option: IMatchPlayer, selectedVals?: IMatchPlayer[]): IMatchPlayer {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILineup>>): void {
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

  protected updateForm(lineup: ILineup): void {
    this.editForm.patchValue({
      id: lineup.id,
      callup: lineup.callup,
      formation: lineup.formation,
      players: lineup.players,
    });

    this.callupsCollection = this.callupService.addCallupToCollectionIfMissing(this.callupsCollection, lineup.callup);
    this.formationsSharedCollection = this.formationService.addFormationToCollectionIfMissing(
      this.formationsSharedCollection,
      lineup.formation
    );
    this.matchPlayersSharedCollection = this.matchPlayerService.addMatchPlayerToCollectionIfMissing(
      this.matchPlayersSharedCollection,
      ...(lineup.players ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.callupService
      .query({ filter: 'lineup-is-null' })
      .pipe(map((res: HttpResponse<ICallup[]>) => res.body ?? []))
      .pipe(map((callups: ICallup[]) => this.callupService.addCallupToCollectionIfMissing(callups, this.editForm.get('callup')!.value)))
      .subscribe((callups: ICallup[]) => (this.callupsCollection = callups));

    this.formationService
      .query()
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing(formations, this.editForm.get('formation')!.value)
        )
      )
      .subscribe((formations: IFormation[]) => (this.formationsSharedCollection = formations));

    this.matchPlayerService
      .query()
      .pipe(map((res: HttpResponse<IMatchPlayer[]>) => res.body ?? []))
      .pipe(
        map((matchPlayers: IMatchPlayer[]) =>
          this.matchPlayerService.addMatchPlayerToCollectionIfMissing(matchPlayers, ...(this.editForm.get('players')!.value ?? []))
        )
      )
      .subscribe((matchPlayers: IMatchPlayer[]) => (this.matchPlayersSharedCollection = matchPlayers));
  }

  protected createFromForm(): ILineup {
    return {
      ...new Lineup(),
      id: this.editForm.get(['id'])!.value,
      callup: this.editForm.get(['callup'])!.value,
      formation: this.editForm.get(['formation'])!.value,
      players: this.editForm.get(['players'])!.value,
    };
  }
}
